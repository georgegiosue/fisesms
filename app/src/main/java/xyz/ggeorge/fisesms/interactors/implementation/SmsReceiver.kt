package xyz.ggeorge.fisesms.interactors.implementation

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.telephony.SmsMessage
import androidx.activity.ComponentActivity
import androidx.lifecycle.ViewModelProvider
import xyz.ggeorge.core.domain.SMS
import xyz.ggeorge.core.domain.events.AppEvent
import xyz.ggeorge.fisesms.framework.ui.viewmodels.FiseViewModel

class SmsReceiver() : BroadcastReceiver() {

    companion object {
        const val INTENT_FILTER = "android.provider.Telephony.SMS_RECEIVED"
    }

    @SuppressLint("UnsafeProtectedBroadcastReceiver")
    override fun onReceive(context: Context, intent: Intent) {

        val bundle = intent.extras
        try {
            if (bundle != null) {
                val pdusObj = bundle.get("pdus") as Array<*>
                for (i in pdusObj.indices) {
                    val currentMessage = SmsMessage.createFromPdu(pdusObj[i] as ByteArray)
                    val phoneNumber = currentMessage.displayOriginatingAddress
                    val message = currentMessage.displayMessageBody

                    val sms = SMS(phoneNumber, message)
                    val viewModel = ViewModelProvider(context as ComponentActivity)[FiseViewModel::class.java]
                    if (sms.passSms()) {
                        viewModel.setSms(sms)
                        viewModel.onEvent(AppEvent.SMS_RECEIVED)
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}