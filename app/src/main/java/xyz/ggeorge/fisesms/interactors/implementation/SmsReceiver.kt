package xyz.ggeorge.fisesms.interactors.implementation

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.telephony.SmsMessage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber
import xyz.ggeorge.core.domain.Fise
import xyz.ggeorge.core.domain.SMS
import xyz.ggeorge.core.domain.state.FiseState
import xyz.ggeorge.fisesms.FiseSMSApp
import xyz.ggeorge.fisesms.data.entities.FiseEntity
import xyz.ggeorge.fisesms.data.entities.ProcessingResultEntity
import xyz.ggeorge.fisesms.framework.notifications.FiseNotificationHelper

class SmsReceiver : BroadcastReceiver() {

    companion object {
        const val INTENT_FILTER = "android.provider.Telephony.SMS_RECEIVED"
    }

    private val logger = Timber.tag("SmsReceiver")

    @SuppressLint("UnsafeProtectedBroadcastReceiver")
    override fun onReceive(context: Context, intent: Intent) {
        val bundle = intent.extras ?: return
        val pendingResult = goAsync()

        try {
            val pdusObj = bundle.get("pdus") as? Array<*> ?: return
            for (i in pdusObj.indices) {
                val currentMessage = SmsMessage.createFromPdu(pdusObj[i] as ByteArray)
                val phoneNumber = currentMessage.displayOriginatingAddress
                val message = currentMessage.displayMessageBody

                val sms = SMS(phoneNumber, message)
                if (!sms.passSms()) continue

                val fiseDao = (context.applicationContext as FiseSMSApp).fiseDao
                val state = FiseStateResolver.determine(sms.body)

                CoroutineScope(Dispatchers.IO).launch {
                    try {
                        if (state == FiseState.PROCESSED) {
                            val fise = Fise.fromSMS(sms.body, state) as Fise.Processed
                            fiseDao.upsert(
                                FiseEntity(
                                    code = fise.code,
                                    dni = fise.dni,
                                    amount = fise.amount,
                                )
                            )
                        }

                        fiseDao.upsertResult(
                            ProcessingResultEntity(
                                state = state.name,
                                smsBody = sms.body,
                            )
                        )

                        val details = buildNotificationDetails(state, sms.body)
                        FiseNotificationHelper.showResultNotification(context, state, details)
                    } catch (e: Exception) {
                        logger.e(e, "Error procesando SMS en background")
                    } finally {
                        pendingResult.finish()
                    }
                }
            }
        } catch (e: Exception) {
            logger.e(e, "Error en onReceive")
            pendingResult.finish()
        }
    }

    private fun buildNotificationDetails(state: FiseState, body: String): String {
        return try {
            when (state) {
                FiseState.PROCESSED -> {
                    val fise = Fise.fromSMS(body, state) as Fise.Processed
                    "Vale: ${fise.code}, DNI: ${fise.dni}, Monto: S/${fise.amount}"
                }
                FiseState.CHECK_BALANCE -> {
                    val balance = body.split(':').getOrNull(3) ?: "N/A"
                    "Saldo disponible: $balance"
                }
                else -> body.take(100)
            }
        } catch (e: Exception) {
            body.take(100)
        }
    }
}
