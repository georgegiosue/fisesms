package xyz.ggeorge.components.camera

import android.annotation.SuppressLint
import android.content.Context
import androidx.annotation.OptIn
import androidx.camera.core.Camera
import androidx.camera.core.CameraSelector
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.FileProvider
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.compose.LocalLifecycleOwner
import java.io.File
import java.util.concurrent.Executors

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

    var imageCapture by remember {
        mutableStateOf<ImageCapture?>(null)
    }

    val cameraExecutor = Executors.newSingleThreadExecutor()

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
                        imageCapture
                    )

                    onCamera(camera)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
                previewView
            }
        )

        Button(
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
                .align(Alignment.BottomCenter)
                .padding(16.dp)
        ) {
            Text(text = "Capturar")
        }
    }
}
