package xyz.ggeorge.components.camera

import android.annotation.SuppressLint
import android.content.Context
import androidx.annotation.OptIn
import androidx.camera.core.Camera
import androidx.camera.core.CameraControl
import androidx.camera.core.CameraSelector
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.FlashOff
import androidx.compose.material.icons.outlined.FlashOn
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedIconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.FileProvider
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.compose.LocalLifecycleOwner
import java.io.File
import java.util.concurrent.Executors
import kotlin.math.roundToInt

@SuppressLint("RestrictedApi")
@OptIn(ExperimentalGetImage::class)
@Composable
fun CameraScreen(
    modifier: Modifier = Modifier,
    contextCurrent: Context = LocalContext.current,
    lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current,
    imageCaptureBuilder: ImageCapture.Builder = ImageCapture.Builder(),
    onCamera: (camera: Camera?) -> Unit,
    onPhotoCaptured: (String?) -> Unit
) {
    val cameraProviderFuture = remember {
        ProcessCameraProvider.getInstance(contextCurrent)
    }

    var imageCapture by remember { mutableStateOf<ImageCapture?>(null) }
    var flashEnabled by remember { mutableStateOf(false) } // Estado del flash
    var cameraControl: CameraControl? by remember { mutableStateOf(null) }
    val cameraExecutor = Executors.newSingleThreadExecutor()
    var zoomLevel by remember { mutableStateOf(1f) }
    val outputDirectory = contextCurrent.cacheDir
    val photoFile = File(outputDirectory, "${System.currentTimeMillis()}.jpg")
    val photoUri = FileProvider.getUriForFile(
        contextCurrent,
        "${contextCurrent.packageName}.provider",
        photoFile
    )

    Box(modifier = modifier.fillMaxSize()) {
        AndroidView(
            modifier = Modifier.fillMaxSize(),
            factory = { context ->
                val preview = Preview.Builder().build()
                val previewView = PreviewView(context)
                val selector = CameraSelector.DEFAULT_BACK_CAMERA
                val currentImageCapture = imageCaptureBuilder
                    .setCameraSelector(selector)
                    .build()

                imageCapture = currentImageCapture

                preview.setSurfaceProvider(previewView.surfaceProvider)

                try {
                    val cameraProvider = cameraProviderFuture.get()
                    val camera = cameraProvider.bindToLifecycle(
                        lifecycleOwner,
                        selector,
                        preview,
                        currentImageCapture
                    )

                    cameraControl = camera.cameraControl

                    onCamera(camera)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
                previewView
            }
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .padding(2.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {

            Row(
                modifier = Modifier
                    .padding(1.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ) {

                OutlinedIconButton(
                    border = BorderStroke(1.dp, if (flashEnabled) Color.Yellow else Color.White),
                    shape = CircleShape,
                    colors = IconButtonDefaults.outlinedIconButtonColors(
                        containerColor = if (flashEnabled) Color.Yellow else Color.Transparent,
                        contentColor = if (flashEnabled) Color.Transparent else Color.Yellow
                    ),
                    onClick = {
                        if (cameraControl != null) {
                            flashEnabled = !flashEnabled
                            try {
                                cameraControl?.enableTorch(flashEnabled)
                            } catch (e: Exception) {
                                e.printStackTrace()
                            }
                        }
                    },
                ) {
                    if (flashEnabled) {
                        Icon(
                            imageVector = Icons.Outlined.FlashOn,
                            contentDescription = "Flash",
                            tint = Color.DarkGray
                        )
                    } else {
                        Icon(
                            imageVector = Icons.Outlined.FlashOff,
                            contentDescription = "Flash",
                            tint = Color.White
                        )
                    }
                }

                OutlinedIconButton(
                    border = BorderStroke(1.dp, Color.White),
                    shape = CircleShape,
                    colors = IconButtonDefaults.outlinedIconButtonColors(
                        containerColor = Color.Transparent,
                        contentColor = Color.White
                    ),
                    onClick = {
                        zoomLevel = if (zoomLevel == 1f) 2f else 1f

                        cameraControl?.let {
                            try {
                                it.setZoomRatio(zoomLevel)
                            } catch (e: Exception) {
                                e.printStackTrace()
                            }
                        }
                    },
                    modifier = Modifier
                        .padding(16.dp)
                ) {
                    Text(
                        text = "${zoomLevel.roundToInt()}x",
                        style = TextStyle(
                            fontStyle = FontStyle.Italic,
                            fontFamily = FontFamily.Monospace,
                            color = MaterialTheme.colorScheme.onPrimary,
                            fontSize = 12.sp
                        )
                    )
                }
            }

            OutlinedButton(
                border = BorderStroke(1.dp, Color.White),
                onClick = {
                    imageCapture?.takePicture(
                        ImageCapture.OutputFileOptions.Builder(photoFile).build(),
                        cameraExecutor,
                        object : ImageCapture.OnImageSavedCallback {
                            override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                                try {
                                    onPhotoCaptured(photoFile.absolutePath)
                                } catch (e: Exception) {
                                    e.printStackTrace()
                                    onPhotoCaptured(null)
                                }
                            }

                            override fun onError(exception: ImageCaptureException) {
                                exception.printStackTrace()
                                onPhotoCaptured(null)
                            }
                        }
                    )
                },
                modifier = Modifier

                    .padding(16.dp)
            ) {
                Text(
                    text = "Capturar",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onPrimary
                )
            }
        }
    }
}