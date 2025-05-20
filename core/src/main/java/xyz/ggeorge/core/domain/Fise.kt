package xyz.ggeorge.core.domain

import xyz.ggeorge.core.domain.state.FiseState
import xyz.ggeorge.core.util.FiseRegex
import java.text.SimpleDateFormat
import java.util.Date

sealed class Fise(var state: FiseState) {

    companion object {
        val SERVICE_PHONE_NUMBERS = listOf("55555", "58996")
        const val SERVICE_CODE = "fise ah01"
        const val BALANCE = "Saldo ah01"

        fun fromSMS(sms: SMS, fiseState: FiseState): Fise {

            val smsBody = sms.body

            return when (fiseState) {
                FiseState.PROCESSED -> {
                    with(FiseRegex.PROCESSED) {
                        val dni = (DNI match smsBody)?.groupValues?.get(1)!!
                        val vale = (VALE match smsBody)?.groupValues?.get(1)!!
                        val amount = (AMOUNT match smsBody)?.groupValues?.get(1)!!.toDouble()

                        return Processed(dni = dni, code = vale, amount = amount)
                    }
                }

                FiseState.PREVIOUSLY_PROCESSED -> {
                    with(FiseRegex.PREVIOUSLY_PROCESSED) {
                        val dateString = (DATE match smsBody)?.value
                        val agentDNI = (AGENT_DNI match smsBody)?.value?.substring(6)!!

                        val dateFormat = SimpleDateFormat(
                            "yyyy/MM/dd HH:mm:ss",
                            java.util.Locale.getDefault()
                        )
                        val date = dateFormat.parse(dateString)

                        return PreviouslyProcessed(date = date, agentDNI = agentDNI)
                    }
                }

                FiseState.WRONG -> Wrong(smsBody)
                else -> SyntaxError(smsBody)
            }
        }
    }

    data class ToSend(
        var dni: String,
        var code: String,
    ) : Fise(FiseState.TO_SEND) {
        fun payload(): String = "$SERVICE_CODE $dni $code"

        companion object {
            const val DNI_LENGTH = 8
            const val CODE_LENGTH = 13
            const val STANDARD_PROCESSED_LENGTH =
                SERVICE_CODE.length + 1 + DNI_LENGTH + 1 + CODE_LENGTH
        }
    }

    data class Processed(
        var dni: String,
        var code: String,
        var amount: Double?,
        val processedTimestamp: String? = null
    ) : Fise(FiseState.PROCESSED)

    data class PreviouslyProcessed(
        var date: Date?,
        var agentDNI: String
    ) : Fise(FiseState.PREVIOUSLY_PROCESSED)

    data class Wrong(
        var msg: String
    ) : Fise(FiseState.WRONG)

    data class SyntaxError(
        var msg: String
    ) : Fise(FiseState.SYNTAX_ERROR)
}
