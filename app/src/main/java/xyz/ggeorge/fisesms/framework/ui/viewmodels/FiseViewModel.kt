package xyz.ggeorge.fisesms.framework.ui.viewmodels

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import timber.log.Timber
import xyz.ggeorge.core.domain.Fise
import xyz.ggeorge.core.domain.SMS
import xyz.ggeorge.core.domain.events.AppEvent
import xyz.ggeorge.core.domain.events.ErrorEvent
import xyz.ggeorge.core.domain.exceptions.FiseError
import xyz.ggeorge.core.domain.state.BalanceState
import xyz.ggeorge.core.domain.state.FiseState
import xyz.ggeorge.core.domain.state.ProcessState
import xyz.ggeorge.fisesms.data.api.uploadImageToApi
import xyz.ggeorge.fisesms.data.dao.FiseDao
import xyz.ggeorge.fisesms.data.entities.FiseEntity
import xyz.ggeorge.fisesms.framework.ui.lib.decodeBase64ToBitmap
import xyz.ggeorge.fisesms.framework.ui.lib.toast
import xyz.ggeorge.fisesms.framework.ui.state.CouponsState
import xyz.ggeorge.fisesms.framework.ui.state.SortType
import xyz.ggeorge.fisesms.framework.ui.viewmodels.SettingsViewModel.Companion.SERVICE_NUMBER_KEY
import xyz.ggeorge.fisesms.interactors.implementation.SMSManager

@OptIn(ExperimentalCoroutinesApi::class)
class FiseViewModel(private val fiseDao: FiseDao) : ViewModel() {

    private val logger = Timber.tag("FiseViewModel")

    private val _fise = MutableStateFlow<Fise>(Fise.ToSend("", ""))
    val fise: StateFlow<Fise> = _fise.asStateFlow()

    private val _lastFiseSent = MutableStateFlow<Fise.ToSend?>(null)
    val lastFiseSent: StateFlow<Fise.ToSend?> = _lastFiseSent.asStateFlow()

    private val _sortType = MutableStateFlow(SortType.PROCESS_TIMESTAMP_DESC)

    private val _aiImagePath = MutableStateFlow<String?>(null)
    val aiImagePath: StateFlow<String?> = _aiImagePath.asStateFlow()

    private val _aiCouponValue = MutableStateFlow("")
    val aiCouponValue: StateFlow<String> = _aiCouponValue.asStateFlow()

    private val _onAIResult = MutableStateFlow(false)
    val onAIResult: StateFlow<Boolean> = _onAIResult.asStateFlow()

    private val _aiDniValue = MutableStateFlow("")
    val aiDniValue: StateFlow<String> = _aiDniValue.asStateFlow()

    private val _processingTimeSeconds = MutableStateFlow(0F)
    val processingTimeSeconds: StateFlow<Float> = _processingTimeSeconds.asStateFlow()

    private val _coupons = _sortType
        .flatMapLatest { sortType ->
            when (sortType) {
                SortType.PROCESS_TIMESTAMP_ASC -> fiseDao.getAllOrderedByProcessedTimestampAsc()
                SortType.PROCESS_TIMESTAMP_DESC -> fiseDao.getAllOrderedByProcessedTimestampDesc()
            }
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())

    private val _state = MutableStateFlow(CouponsState())

    val state = combine(_state, _sortType, _coupons) { state, sortType, coupons ->
        state.copy(
            coupons = coupons,
            sortType = sortType
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), CouponsState())

    private val _balance = MutableStateFlow("")
    val balance: StateFlow<String> = _balance.asStateFlow()

    private val _processState = MutableStateFlow(ProcessState.INITIAL)
    val processState: StateFlow<ProcessState> = _processState.asStateFlow()

    private val _balanceState = MutableStateFlow(BalanceState.INITIAL)
    val balanceState: StateFlow<BalanceState> = _balanceState.asStateFlow()

    private val smsManager = SMSManager()

    private val sms = MutableStateFlow<SMS?>(SMS("", ""))

    private val _fiseError = MutableStateFlow(FiseError(""))
    val fiseError: StateFlow<FiseError> = _fiseError.asStateFlow()

    private val TOKEN_SPLIT_REGEX: Regex = "\\s+".toRegex()

    private fun setProcessState(value: ProcessState) {
        _processState.value = value
    }

    private fun setBalanceState(value: BalanceState) {
        _balanceState.value = value
    }

    fun setFise(fise: Fise) {
        _fise.value = fise
    }

    fun setSms(sms: SMS?) {
        this.sms.value = sms
    }

    private fun smsToFise(sms: SMS, fiseState: FiseState): Fise {

        return Fise.fromSMS(sms.body, fiseState)
    }

    fun setAIImagePath(imagePath: String?) {
        _aiImagePath.value = imagePath
    }

    fun setOnAIResult(value: Boolean) {
        _onAIResult.value = value
    }

    /**
     * Determina el estado FISE a partir del contenido del SMS.
     *
     * @param body Texto completo del SMS recibido (puede ser null o vacío).
     * @return Estado inferido según el primer o segundo token.
     */
    fun determinateState(): FiseState {
        // 1) Normalizamos y tokenizamos de forma segura
        val tokens: List<String> = sms.value?.body
            ?.trim()
            ?.split(TOKEN_SPLIT_REGEX)
            .orEmpty()

        // 2) Si el segundo token es "saldo", vamos a CHECK_BALANCE
        tokens.getOrNull(1)
            ?.takeIf { it.equals("saldo", ignoreCase = true) }
            ?.let { return FiseState.CHECK_BALANCE }

        // 3) Branch principal según el primer token
        return when (tokens.firstOrNull()?.uppercase()) {
            "EL" -> FiseState.PROCESSED
            "VALE" -> FiseState.PREVIOUSLY_PROCESSED
            "DOC.BENEF." -> FiseState.WRONG
            else -> {
                // 4) Log de diagnóstico en desarrollo
                logger.w("Token inesperado: ${tokens.firstOrNull()}")
                FiseState.SYNTAX_ERROR
            }
        }
    }

    private fun extractBalanceFromSms(sms: SMS): String = sms.body.split(':')[3]

    fun onEvent(event: AppEvent, ctx: Context? = null) {
        when (event) {
            AppEvent.SMS_RECEIVED -> {

                val state: FiseState = determinateState()

                if (state == FiseState.CHECK_BALANCE) {
                    _balance.value = extractBalanceFromSms(sms.value!!)

                    setBalanceState(BalanceState.BALANCE_CHECKED)
                } else {
                    _fise.value = smsToFise(sms.value!!, state)

                    setProcessState(ProcessState.COUPON_RECEIVED)

                    if (state == FiseState.PROCESSED) {
                        viewModelScope.launch {

                            val fiseProcessed = fise.value as Fise.Processed

                            fiseDao.upsert(
                                FiseEntity(
                                    code = fiseProcessed.code,
                                    dni = fiseProcessed.dni,
                                    amount = fiseProcessed.amount,
                                )
                            )
                        }
                    }
                }

            }

            AppEvent.PROCESS_COUPON -> {

                viewModelScope.launch {

                    val fiseToSend = fise.value as Fise.ToSend

                    try {

                        val serviceNumber = ctx?.dataStore!!.data.map { preferences ->
                            preferences[SERVICE_NUMBER_KEY] ?: "55555"
                        }.first()

                        smsManager.send(
                            SMS(
                                serviceNumber,
                                fiseToSend.payload()
                            ),
                            ctx!!
                        )

                        setProcessState(ProcessState.PROCESSING_COUPON)

                        _lastFiseSent.value = fiseToSend

                        ctx.toast("Mensaje Enviado")
                    } catch (exception: Exception) {

                        onErrorEvent(ErrorEvent.ON_ERROR_PROCESSING_COUPON, exception)
                    }
                }
            }

            AppEvent.CHECK_BALANCE -> {

                viewModelScope.launch {

                    try {

                        val serviceNumber = ctx?.dataStore!!.data.map { preferences ->
                            preferences[SERVICE_NUMBER_KEY] ?: "55555"
                        }.first()

                        smsManager.send(
                            SMS(
                                serviceNumber,
                                Fise.BALANCE
                            ),
                            ctx!!
                        )

                        setBalanceState(BalanceState.CHECKING_BALANCE)

                        ctx.toast("Mensaje Enviado")
                    } catch (exception: Exception) {

                        onErrorEvent(ErrorEvent.ON_ERROR_CHECKING_BALANCE, exception)
                    }
                }
            }

            AppEvent.AI_PROCESS -> {

                val image = aiImagePath.value!!

                _processingTimeSeconds.value = 0.00F

                uploadImageToApi(
                    image,
                    onSuccess = { couponRegion, couponCode, dniRegion, dniNumber, processingTime ->

                        val decodedCouponImage = decodeBase64ToBitmap(couponRegion)
                        val decodedDniImage = decodeBase64ToBitmap(dniRegion)

                        _aiCouponValue.value = couponCode
                        _aiDniValue.value = dniNumber
                        val processingTimeAsFloat =
                            processingTime.split(" ").firstOrNull()?.toFloatOrNull()
                        _processingTimeSeconds.value = processingTimeAsFloat ?: 0F

                        _onAIResult.value = true

                    },
                    onError = { errorMessage ->

                        onErrorEvent(ErrorEvent.ON_ERROR_AI_PROCESS, Exception(errorMessage))
                    })
            }
        }
    }

    fun onErrorEvent(errorEvent: ErrorEvent, exception: Exception) {

        when (errorEvent) {
            ErrorEvent.ON_ERROR_CHECKING_BALANCE -> {

                _fiseError.value =
                    FiseError("${exception.message} | Ocurrio un error al verificar el saldo de su cuenta ")


                setBalanceState(BalanceState.ERROR_CHECKING_BALANCE)
            }

            ErrorEvent.ON_ERROR_PROCESSING_COUPON -> {

                _fiseError.value =
                    FiseError("${exception.message} | Por favor, verifique el DNI y/o el Codigo del Vale")

                setProcessState(ProcessState.ERROR_PROCESSING_COUPON)
            }

            ErrorEvent.ON_ERROR_AI_PROCESS -> {

                _fiseError.value =
                    FiseError("${exception.message} | Ocurrio un error al procesar la imagen")

                setProcessState(ProcessState.ERROR_AI_PROCESS)
            }
        }
    }
}