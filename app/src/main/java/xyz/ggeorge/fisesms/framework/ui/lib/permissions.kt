package xyz.ggeorge.fisesms.framework.ui.lib

import android.os.Build
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun RequestPermissions(onDismissRequest: () -> Unit) {
    val permissions = buildList {
        add(PERMISSIONS.CAMERA)
        add(PERMISSIONS.SEND_SMS)
        add(PERMISSIONS.RECEIVE_SMS)
        add(PERMISSIONS.INTERNET)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            add(PERMISSIONS.POST_NOTIFICATIONS)
        }
    }

    val multiplePermissionsState = rememberMultiplePermissionsState(permissions)

    if (!multiplePermissionsState.allPermissionsGranted) {
        AlertDialog(onDismissRequest = { }, confirmButton = {
            FilledTonalButton(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                contentPadding = PaddingValues(vertical = 16.dp),
                onClick = { multiplePermissionsState.launchMultiplePermissionRequest() }
            ) {
                Text(
                    text = "Autorizar permisos",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
            }
        }, dismissButton = {
            FilledTonalButton(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                contentPadding = PaddingValues(vertical = 16.dp),
                onClick = { onDismissRequest() }
            ) {
                Text(
                    text = "Cancelar",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
            }
        }, title = {
            Text(
                text = "Permisos de Camara, SMS, Internet y Notificaciones",
                style = TextStyle(
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )
            )
        }, text = {
            Text(
                text = "Los permisos de la camara, enviar / recibir SMS, Internet y notificaciones estan denegados. Por favor, autoriza los permisos para continuar.",
                style = TextStyle(
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Normal
                )
            )
        })
    }
}
