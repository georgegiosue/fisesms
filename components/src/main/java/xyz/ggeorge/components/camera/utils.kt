package xyz.ggeorge.components.camera

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import java.io.File
import java.io.FileOutputStream

fun compressImage(file: File): File? {
    val bitmap = BitmapFactory.decodeFile(file.absolutePath)

    val originalWidth = bitmap.width
    val originalHeight = bitmap.height

    val maxDimension = 1024

    val scaleFactor = if (originalWidth > originalHeight) {
        maxDimension.toFloat() / originalWidth
    } else {
        maxDimension.toFloat() / originalHeight
    }

    val newWidth = (originalWidth * scaleFactor).toInt()
    val newHeight = (originalHeight * scaleFactor).toInt()

    val resizedBitmap = Bitmap.createScaledBitmap(bitmap, newWidth, newHeight, false)

    val compressedFile = File(file.parent, "compressed_${file.name}")
    val outputStream = FileOutputStream(compressedFile)

    resizedBitmap.compress(Bitmap.CompressFormat.JPEG, 70, outputStream)

    outputStream.flush()
    outputStream.close()

    return compressedFile
}