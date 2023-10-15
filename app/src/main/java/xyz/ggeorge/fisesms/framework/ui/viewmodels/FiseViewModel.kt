package xyz.ggeorge.fisesms.framework.ui.viewmodels

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import xyz.ggeorge.core.domain.Fise
import xyz.ggeorge.core.domain.SMS
import xyz.ggeorge.core.state.FiseState
import xyz.ggeorge.fisesms.interactors.implementation.SMSManager

class FiseViewModel() : ViewModel() {

    private val _fise = mutableStateOf(Fise.ToSend("", ""))
    val fise: State<Fise.ToSend> = _fise

    private val _currentFise = mutableStateOf(null as Fise?)
    val currentFise: State<Fise?> = _currentFise

    private val _currentBalance = mutableStateOf("")
    val currentBalance: State<String> = _currentBalance

    private val _onProcessed = mutableStateOf(false)
    val onProcessed: State<Boolean> = _onProcessed

    private val _smsManager = SMSManager()
    val smsManager = _smsManager

    private val _smsReceived = mutableStateOf<SMS?>(SMS("", ""))
    val smsReceived: State<SMS?> = _smsReceived

    val count = mutableStateOf(0)

    fun statusChange() = count.value++

    fun setSmsReceived(sms: SMS?) {
        _smsReceived.value = sms
    }

    fun getLastSendFise(): Fise? {

        val state: FiseState = determinateState() ?: return currentFise.value
        _currentFise.value = Fise.fromSMS(smsReceived.value!!, state)
        return currentFise.value
    }

    fun setCurrentFise(value: Fise?) {
        _currentFise.value = value
    }

    private fun determinateState(): FiseState? {

        val smsBodyChars = _smsReceived.value?.body?.split(' ')

        try {
            if (smsBodyChars?.get(1) == "saldo") return null
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

    fun getBalance(): String {

        if(determinateState() != null) return currentBalance.value

        var currentValue = ""

        return try {
            currentValue = _smsReceived.value?.body?.split(':')?.get(3)!!
            _currentBalance.value = currentValue
            currentValue
        } catch (e: Exception) {
            currentBalance.value
        }
    }

    fun setOnProcessing(value: Boolean) {
        _onProcessed.value = value
    }
}