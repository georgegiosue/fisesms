package xyz.ggeorge.fisesms.data.api

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File

fun uploadImageToApi(
    filePath: String?,
    onSuccess: (couponRegion: String, couponCode: String, dniRegion: String, dniNumber: String, processingTime: String) -> Unit,
    onError: (errorMessage: String) -> Unit
) {

    if (filePath.isNullOrEmpty()) {
        onError("Error: La ruta del archivo es nula o vacía.")
        return
    }

    val file = File(filePath)

    if (!file.exists()) {
        onError("Error: Archivo no encontrado en la ruta especificada.")
        return
    }

    val requestBody = RequestBody.create("image/jpeg".toMediaTypeOrNull(), file)
    val multipartBody = MultipartBody.Part.createFormData("file", file.name, requestBody)

    val apiService = RetrofitClient.instance

    CoroutineScope(Dispatchers.IO).launch {
        try {
            val response = apiService.uploadImage(multipartBody)
            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    val apiResponse = response.body()
                    if (apiResponse != null) {
                        onSuccess(
                            apiResponse.coupon.region,
                            apiResponse.coupon.code,
                            apiResponse.dni.region,
                            apiResponse.dni.number,
                            apiResponse.metadata.processing_time
                        )
                    } else {
                        onError("Error al obtener la respuesta del servidor.")
                    }
                } else {
                    onError("Error al enviar la imagen: ${response.message()}")
                }
            }
        } catch (e: Exception) {
            withContext(Dispatchers.Main) {
                e.printStackTrace()
                onError("Error al enviar la imagen: ${e.message ?: "Detalles no disponibles"}")
            }
        }
    }
}