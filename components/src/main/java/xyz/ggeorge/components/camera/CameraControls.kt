package xyz.ggeorge.components.camera

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FlashOff
import androidx.compose.material.icons.filled.FlashOn
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun CameraUIControls(
    isFlashEnabled: Boolean,
    zoom: ZoomLevel,
    updateStates: (Boolean, ZoomLevel) -> Unit
) {
    Row(modifier = Modifier.padding(8.dp), horizontalArrangement = Arrangement.spacedBy(4.dp)) {
        FilledTonalIconButton(colors = IconButtonDefaults.filledTonalIconButtonColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer.copy(
                alpha = 0.8f
            )
        ), onClick = {
            updateStates(
                !isFlashEnabled,
                zoom
            )
        }) {
            Icon(
                imageVector = if (isFlashEnabled) Icons.Filled.FlashOn else Icons.Filled.FlashOff,
                contentDescription = "Activar o desactivar Flash",
                tint = MaterialTheme.colorScheme.surfaceTint
            )
        }
        FilledTonalButton(
            colors = ButtonDefaults.filledTonalButtonColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer.copy(
                    alpha = 0.8f
                )
            ), onClick = {
                val newZoom = when (zoom) {
                    ZoomLevel.X1 -> ZoomLevel.X2
                    ZoomLevel.X2 -> ZoomLevel.X1
                }
                updateStates(isFlashEnabled, newZoom)
            }) {
            Text(
                text = zoom.name,
                style = TextStyle(fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
            )
        }
    }
}