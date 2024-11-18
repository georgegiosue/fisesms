package xyz.ggeorge.components.camera

import android.content.Context
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy

class InferenceCouponFiseAnalyzer(ctx: Context) : ImageAnalysis.Analyzer {

    override fun analyze(image: ImageProxy) {
        // Perform inference here
    }
}
