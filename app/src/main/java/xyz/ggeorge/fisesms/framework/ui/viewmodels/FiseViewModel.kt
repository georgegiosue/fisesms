package xyz.ggeorge.fisesms.framework.ui.viewmodels

import android.content.Context
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import xyz.ggeorge.core.domain.Fise
import xyz.ggeorge.core.domain.SMS
import xyz.ggeorge.core.domain.state.FiseState
import xyz.ggeorge.fisesms.data.dao.FiseDao
import xyz.ggeorge.fisesms.data.entities.FiseEntity
import xyz.ggeorge.core.domain.events.AppEvent
import xyz.ggeorge.core.domain.state.BalanceState
import xyz.ggeorge.core.domain.events.ErrorEvent
import xyz.ggeorge.core.domain.exceptions.FiseError
import xyz.ggeorge.core.domain.state.ProcessState
import xyz.ggeorge.fisesms.framework.ui.lib.toast
import xyz.ggeorge.fisesms.interactors.implementation.SMSManager

class FiseViewModel(private val fiseDao: FiseDao) : ViewModel() {

    private val _fise = mutableStateOf<Fise>(Fise.ToSend("", ""))
    val fise: State<Fise> = _fise

    private val _lastFiseSent = mutableStateOf<Fise.ToSend?>(null)
    val lastFiseSent: State<Fise.ToSend?> = _lastFiseSent

    private val _balance = mutableStateOf("")
    val balance: State<String> = _balance

    private val _processState = mutableStateOf(ProcessState.INITIAL)
    val processState: State<ProcessState> = _processState

    private val _balanceState = mutableStateOf(BalanceState.INITIAL)
    val balanceState: State<BalanceState> = _balanceState

    private val smsManager = SMSManager()

    private val sms = mutableStateOf<SMS?>(SMS("", ""))

    private val _fiseError = mutableStateOf(FiseError(""))
    val fiseError: State<FiseError> = _fiseError

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

        return Fise.fromSMS(sms, fiseState)
    }

    private fun determinateState(): FiseState {

        val smsBodyChars = sms.value?.body?.split(' ')

        try {
            if (smsBodyChars?.get(1) == "saldo") return FiseState.CHECK_BALANCE
        } catch (e: IndexOutOfBoundsException) {
            e.printStackTrace()
        }

        return when (smsBodyChars?.get(0)) {
            "El" -> FiseState.PROCESSED
            "VALE" -> FiseState.PREVIOUSLY_PROCESSED
            "DOC.BENEF." -> FiseState.WRONG
            else -> FiseState.SYNTAX_ERROR
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
                }
                else {
                    _fise.value = smsToFise(sms.value!!, state)

                    setProcessState(ProcessState.COUPON_RECEIVED)
                }

            }
            AppEvent.PROCESS_COUPON -> {

                viewModelScope.launch {

                    val fiseToSend = fise.value as Fise.ToSend

                    try {
                        smsManager.send(
                            SMS(
                                Fise.SERVICE_PHONE_NUMBER,
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

                        smsManager.send(
                            SMS(
                                Fise.SERVICE_PHONE_NUMBER,
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
        }
    }

    fun onErrorEvent(errorEvent: ErrorEvent, exception: Exception) {

        when (errorEvent) {
            ErrorEvent.ON_ERROR_CHECKING_BALANCE -> {

                _fiseError.value = FiseError("${exception.message} | Ocurrio un error al verificar el saldo de su cuenta ")


                setBalanceState(BalanceState.ERROR_CHECKING_BALANCE)
            }
            ErrorEvent.ON_ERROR_PROCESSING_COUPON -> {

                _fiseError.value = FiseError("${exception.message} | Por favor, verifique el DNI y/o el Codigo del Vale")

                setProcessState(ProcessState.ERROR_PROCESSING_COUPON)
            }
        }
    }

    fun testDatabase() {
        val randomCode = (100000000..999999999).random().toString()
        viewModelScope.launch {
            fiseDao.upsert(
                FiseEntity(
                    code = randomCode,
                    dni = "12345678"
                )
            )
        }
    }
}