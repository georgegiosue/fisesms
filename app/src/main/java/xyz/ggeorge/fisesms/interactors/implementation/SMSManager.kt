package xyz.ggeorge.fisesms.interactors.implementation

import android.content.Context
import android.telephony.SmsManager
import xyz.ggeorge.core.domain.Fise
import xyz.ggeorge.core.domain.SMS
import xyz.ggeorge.fisesms.interactors.abstraction.ISMSManager


class SMSManager : ISMSManager {
    override fun checkBefore(sms: SMS): Boolean {

        if (!sms.passSms()) throw Exception("El número de teléfono (${sms.phone}) no es el correcto")

        return sms.body.length == Fise.ToSend.STANDARD_PROCESSED_LENGTH || sms.body.length == Fise.BALANCE.length
    }

    override suspend fun send(sms: SMS, context: Context) {
        val smsManager: SmsManager = SmsManager.getDefault()

        if (checkBefore(sms))
            smsManager.sendTextMessage(
                sms.phone,
                null,
                sms.body,
                null,
                null
            )
        else throw Exception("Error al enviar el mensaje")
    }
}