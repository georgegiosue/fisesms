package xyz.ggeorge.components.camera

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import androidx.camera.core.Camera
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.compose.LocalLifecycleOwner
import java.util.concurrent.Executors

@SuppressLint("RestrictedApi")
@Composable
fun TestCamera(
    modifier: Modifier = Modifier,
    contextCurrent: Context = LocalContext.current,
    lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current,
    imageCaptureBuilder: ImageCapture.Builder = ImageCapture.Builder(),
) {
    val cameraProviderFuture = remember { ProcessCameraProvider.getInstance(contextCurrent) }
    var imageCapture by remember { mutableStateOf<ImageCapture?>(null) }
    var camera: Camera? by remember { mutableStateOf(null) }
    var zoomRatio by remember { mutableStateOf(1f) }  // Nivel de zoom inicial
    var isTorchEnabled by remember { mutableStateOf(false) }  // Estado de la linterna

    val cameraExecutor = Executors.newSingleThreadExecutor()

    Column {
        AndroidView(
            modifier = modifier,
            factory = { context ->
                val previewView = PreviewView(context)
                val preview = Preview.Builder().build()
                val selector = CameraSelector.DEFAULT_BACK_CAMERA
                val currentImageCapture = imageCaptureBuilder.setCameraSelector(selector).build()
                imageCapture = currentImageCapture
                preview.setSurfaceProvider(previewView.surfaceProvider)

                // Intentamos iniciar la cámara
                cameraProviderFuture.addListener({
                    try {
                        val cameraProvider = cameraProviderFuture.get()
                        camera = cameraProvider.bindToLifecycle(
                            lifecycleOwner,
                            selector,
                            preview,
                            imageCapture
                        )
                    } catch (e: Exception) {
                        Log.e("CameraScreen", "Error al iniciar la cámara: ${e.message}", e)
                    }
                }, ContextCompat.getMainExecutor(context))

                previewView
            }
        )

        // Botón para alternar el zoom entre 1x y 2x
        Button(onClick = {
            camera?.let {
                zoomRatio = if (zoomRatio == 1f) 2f else 1f
                it.cameraControl.setZoomRatio(zoomRatio)
            } ?: Log.e("CameraScreen", "Error: cámara no inicializada")
        }) {
            Text(text = "Zoom: ${zoomRatio}x")
        }

        // Botón para alternar la linterna entre encendida y apagada
        Button(onClick = {
            camera?.let {
                isTorchEnabled = !isTorchEnabled
                it.cameraControl.enableTorch(isTorchEnabled)
            } ?: Log.e("CameraScreen", "Error: cámara no inicializada")
        }) {
            Text(text = if (isTorchEnabled) "Apagar Linterna" else "Encender Linterna")
        }
    }
}
