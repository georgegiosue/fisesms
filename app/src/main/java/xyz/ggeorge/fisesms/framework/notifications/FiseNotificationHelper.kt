package xyz.ggeorge.fisesms.framework.notifications

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import xyz.ggeorge.core.domain.state.FiseState
import xyz.ggeorge.fisesms.R
import xyz.ggeorge.fisesms.framework.ui.activities.MainActivity

object FiseNotificationHelper {

    private const val CHANNEL_ID = "fise_results_channel"
    private const val NOTIFICATION_ID = 1001

    fun createChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "Resultados FISE",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Notificaciones de resultados de procesamiento de vales FISE"
            }
            val manager = context.getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(channel)
        }
    }

    fun showResultNotification(context: Context, fiseState: FiseState, details: String) {
        val (title, text) = when (fiseState) {
            FiseState.PROCESSED -> "Vale Procesado" to "El vale fue procesado exitosamente. $details"
            FiseState.PREVIOUSLY_PROCESSED -> "Vale Ya Procesado" to "Este vale ya fue procesado anteriormente. $details"
            FiseState.WRONG -> "DNI Incorrecto" to "El documento del beneficiario no coincide. $details"
            FiseState.SYNTAX_ERROR -> "Error de Sintaxis" to "El mensaje tiene un formato incorrecto. $details"
            FiseState.CHECK_BALANCE -> "Consulta de Saldo" to details
            else -> "Resultado FISE" to details
        }

        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
        }
        val pendingIntent = PendingIntent.getActivity(
            context,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle(title)
            .setContentText(text)
            .setStyle(NotificationCompat.BigTextStyle().bigText(text))
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()

        val manager = context.getSystemService(NotificationManager::class.java)
        manager.notify(NOTIFICATION_ID, notification)
    }
}
