package xyz.ggeorge.fisesms.framework.ui.lib

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.result.contract.ActivityResultContracts

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