package xyz.ggeorge.fisesms.data.api

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody

fun uploadImageToApi(
    imageBytes: ByteArray,
    imageName: String = "captured_image.jpg",
    onSuccess: (couponRegion: String, couponCode: String, dniRegion: String, dniNumber: String, processingTime: String) -> Unit,
    onError: (errorMessage: String) -> Unit
) {
    CoroutineScope(Dispatchers.IO).launch {
        try {

            val requestBody = imageBytes.toRequestBody("image/jpeg".toMediaType())

            val multipartBody = MultipartBody.Part.createFormData("file", imageName, requestBody)

            val response = RetrofitClient.instance.uploadImage(multipartBody)

            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    val responseData = response.body()
                    if (responseData != null) {
                        onSuccess(
                            responseData.coupon.region,
                            responseData.coupon.code,
                            responseData.dni.region,
                            responseData.dni.number,
                            responseData.metadata.processing_time
                        )
                    } else {
                        onError("La respuesta de la API está vacía.")
                    }
                } else {
                    onError("Error de la API: ${response.errorBody()?.string()}")
                }
            }
        } catch (e: Exception) {
            withContext(Dispatchers.Main) {
                onError("Excepción: ${e.message}")
            }
        }
    }
}