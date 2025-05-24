package xyz.ggeorge.core.domain

import xyz.ggeorge.core.domain.state.FiseState
import xyz.ggeorge.core.util.FiseRegex
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

sealed class Fise(var state: FiseState) {

    companion object {
        val SERVICE_PHONE_NUMBERS = listOf("55555", "58996")
        const val SERVICE_CODE = "fise ah01"
        const val BALANCE = "Saldo ah01"


        /**
         * Extrae los datos del SMS según su estado.
         * Lanza IllegalArgumentException si el patrón no coincide.
         */
        fun fromSMS(body: String?, state: FiseState): Fise =
            when (state) {
                FiseState.PROCESSED -> extractProcessed(body)
                FiseState.PREVIOUSLY_PROCESSED -> extractPreviouslyProcessed(body)
                FiseState.WRONG -> Wrong(body.orEmpty())
                FiseState.SYNTAX_ERROR -> SyntaxError(body.orEmpty())
                else -> throw IllegalArgumentException("Estado no soportado: $state")
            }

        private fun extractProcessed(body: String?): Processed {
            val match = FiseRegex.Companion.PROCESSED.pattern.find(body.orEmpty())
                ?: throw IllegalArgumentException("El SMS no coincide con la plantilla PROCESSED")
            val (dni, coupon, amountStr) = match.destructured
            return Processed(
                dni = dni,
                code = coupon,
                amount = amountStr.toDouble()
            )
        }

        private fun extractPreviouslyProcessed(body: String?): PreviouslyProcessed {

            val dateFormat = SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.getDefault())

            val match = FiseRegex.Companion.PREVIOUSLY_PROCESSED.pattern.find(body.orEmpty())
                ?: throw IllegalArgumentException("El SMS no coincide con la plantilla PREVIOUSLY_PROCESSED")
            val (dateStr, agentDni) = match.destructured
            val date = dateFormat.parse(dateStr)
                ?: throw IllegalArgumentException("Error al parsear la fecha: $dateStr")
            return PreviouslyProcessed(
                date = date,
                agentDNI = agentDni
            )
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
