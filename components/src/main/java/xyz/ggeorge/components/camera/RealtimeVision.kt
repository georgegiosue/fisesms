package xyz.ggeorge.components.camera

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun RealtimeVision(modifier: Modifier = Modifier, onPhotoCaptured: (String?) -> Unit) {

    var isFlashEnabled by remember { mutableStateOf(false) }
    var zoomLevel by remember { mutableStateOf(ZoomLevel.X1) }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(4.dp)
            .clip(RoundedCornerShape(8.dp)),
        border = BorderStroke(color = Color.Transparent, width = 0.dp)
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CameraScreen(onCamera = { camera ->
                camera?.cameraControl?.setZoomRatio(zoomLevel.factor)
                camera?.cameraControl?.enableTorch(isFlashEnabled)
            }, onPhotoCaptured = onPhotoCaptured)
        }
    }
}