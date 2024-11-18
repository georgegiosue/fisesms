package xyz.ggeorge.components.camera

import android.annotation.SuppressLint
import android.content.Context
import androidx.annotation.OptIn
import androidx.camera.core.Camera
import androidx.camera.core.CameraSelector
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageCapture
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.compose.LocalLifecycleOwner
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
) {

    val cameraProviderFuture = remember {
        ProcessCameraProvider.getInstance(contextCurrent)
    }

    var imageCapture by remember {
        mutableStateOf<ImageCapture?>(null)
    }

    lateinit var imageAnalyzer: ImageAnalysis

    val cameraExecutor = Executors.newSingleThreadExecutor()

    AndroidView(modifier = Modifier.fillMaxSize(), factory = { context ->
        val preview = Preview.Builder().build()
        val previewView = PreviewView(context)
        val selector = CameraSelector.DEFAULT_BACK_CAMERA
        val currentImageCapture = imageCaptureBuilder
            .setCameraSelector(selector)
            .build()

        imageCapture = currentImageCapture

        preview.setSurfaceProvider(previewView.surfaceProvider)

        imageAnalyzer = ImageAnalysis.Builder()
            // This sets the ideal size for the image to be analyse, CameraX will choose the
            // the most suitable resolution which may not be exactly the same or hold the same
            // aspect ratio .setTargetResolution(Size(176, 144))
            //.setTargetResolution(Size(128, 128))
            // How the Image Analyser should pipe in input, 1. every frame but drop no frame, or
            // 2. go to the latest frame and may drop some frame. The default is 2.
            // STRATEGY_KEEP_ONLY_LATEST. The following line is optional, kept here for clarity
            .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
            .build()
            .also { analysisUseCase: ImageAnalysis ->
                analysisUseCase.setAnalyzer(
                    cameraExecutor,
                    InferenceCouponFiseAnalyzer(ctx = context)
                )
            }

        try {
            val cameraProvider = cameraProviderFuture.get()
            val camera = cameraProvider.bindToLifecycle(
                lifecycleOwner,
                selector,
                preview,
                imageCapture,
                imageAnalyzer
            )

            onCamera(camera)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        previewView
    })
}