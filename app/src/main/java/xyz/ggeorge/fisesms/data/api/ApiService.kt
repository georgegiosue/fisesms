package xyz.ggeorge.fisesms.data.api

import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface ApiService {
    @Multipart
    @POST("/inference")
    suspend fun uploadImage(
        @Part file: MultipartBody.Part
    ): Response<ApiResponse>
}
