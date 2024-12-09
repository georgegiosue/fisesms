package xyz.ggeorge.fisesms.framework.ui.lib

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.provider.OpenableColumns
import android.provider.Settings
import android.util.Base64
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.result.contract.ActivityResultContracts
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream

typealias PERMISSIONS = Manifest.permission

fun Context.toast(text: String) {
    Toast.makeText(
        this,
        text,
        Toast.LENGTH_SHORT
    ).show()
}

fun ComponentActivity.requiredPermissions(
    permissions: List<String>,
    onResult: (Boolean) -> Unit
) {

    val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissionsResult ->
            val isGranted = permissionsResult.values.all { it }
            onResult(isGranted)
        }

    val permissionsToRequest = mutableListOf<String>()

    for (permission in permissions) {
        if (checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) {
            permissionsToRequest.add(permission)
        }
    }

    if (permissionsToRequest.isEmpty()) {
        onResult(true)
    } else {
        requestPermissionLauncher.launch(permissionsToRequest.toTypedArray())
    }
}

fun navigateToAppSettings(context: Context) {
    val intent = Intent()

    intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS

    val uri = Uri.fromParts("package", context.packageName, null)
    intent.data = uri

    context.startActivity(intent)
}

fun Uri.uriToFile(ctx: Context): File? {
    return try {
        val tempFile = File(ctx.cacheDir, "temp_image.jpg")
        val inputStream: InputStream? = ctx.contentResolver.openInputStream(this)
        val outputStream = FileOutputStream(tempFile)

        inputStream?.use { input ->
            outputStream.use { output ->
                input.copyTo(output)
            }
        }
        tempFile
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}

fun Uri.uriToFileDirect(): File? {
    return File(this.path ?: return null)
}

fun decodeBase64ToBitmap(base64: String): Bitmap? {
    return try {
        val decodedBytes = Base64.decode(base64, Base64.DEFAULT)
        BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}

fun getFileFromUri(context: Context, uri: Uri): File? {
    try {
        // Obtén el nombre del archivo del URI si es posible
        val fileName =
            getFileNameFromUri(context, uri) ?: "temp_file_${System.currentTimeMillis()}.jpg"

        // Crear un archivo en el directorio de caché
        val file = File(context.cacheDir, fileName)

        // Abrir el InputStream desde el URI
        context.contentResolver.openInputStream(uri)?.use { inputStream ->
            // Escribir el contenido del InputStream al archivo
            file.outputStream().use { outputStream ->
                inputStream.copyTo(outputStream)
            }
        }
        return file
    } catch (e: Exception) {
        e.printStackTrace()
        return null
    }
}

private fun getFileNameFromUri(context: Context, uri: Uri): String? {
    return context.contentResolver.query(uri, null, null, null, null)?.use { cursor ->
        val nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
        cursor.moveToFirst()
        cursor.getString(nameIndex)
    }
}