package xyz.ggeorge.fisesms.framework.ui.navigation.screens

import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import xyz.ggeorge.fisesms.framework.ui.lib.navigateToAppSettings

@Composable
fun ComponentActivity.RequiredPermissionsPage() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 14.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Text(
            text = "La aplicación no puede funcionar sin permisos de SMS",
            style = TextStyle(
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            ),
            textAlign = TextAlign.Center
        )
        Text(
            text = "Active los permisos de SMS en la configuración y reinicie la aplicación",
            style = TextStyle(
                fontSize = 14.sp,
                fontWeight = FontWeight.Light
            ),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.padding(vertical = 16.dp))

        Button(onClick = {
            navigateToAppSettings(this@RequiredPermissionsPage)
        }) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text = "Ir a la configuración")
                Spacer(modifier = Modifier.padding(horizontal = 4.dp))
                Icon(
                    Icons.Outlined.Settings,
                    contentDescription = "Settings"
                )
            }
        }
    }
}