package xyz.ggeorge.fisesms.interactors.implementation

import android.content.Context
import android.telephony.SmsManager
import xyz.ggeorge.core.domain.Fise
import xyz.ggeorge.core.domain.SMS
import xyz.ggeorge.fisesms.interactors.abstraction.ISMSManager


class SMSManager : ISMSManager {
    override fun checkBefore(sms: SMS): Boolean {
        return sms.phone == Fise.SERVICE_PHONE_NUMBER && (sms.body.length == Fise.ToSend.STANDAR_PROCESSED_LENGTH || sms.body.length == Fise.ToSend.STANDAR_BALANCE_LENGTH)
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
        else throw Exception("Syntax's error, please check the sms content length")
    }
}