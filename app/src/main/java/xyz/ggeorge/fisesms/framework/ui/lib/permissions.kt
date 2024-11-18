package xyz.ggeorge.fisesms.framework.ui.lib

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
fun RequestCameraAndSMSPermissions(onDismissRequest: () -> Unit) {
    val multiplePermissionsState = rememberMultiplePermissionsState(
        listOf(PERMISSIONS.CAMERA, PERMISSIONS.SEND_SMS, PERMISSIONS.RECEIVE_SMS)
    )

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
                text = "Permisos de Camara y SMS",
                style = TextStyle(
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )
            )
        }, text = {
            Text(
                text = "Los permisos de la camara, enviar y recibir SMS estan denegados. Por favor, autoriza los permisos para continuar.",
                style = TextStyle(
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Normal
                )
            )
        })
    }
}
