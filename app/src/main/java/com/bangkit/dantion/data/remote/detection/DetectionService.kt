package com.bangkit.dantion.data.remote.detection

import com.bangkit.dantion.data.remote.ErrorMessageResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*

interface DetectionService {

    @GET("detections")
    suspend fun getAllDetections(
        @Header("Authorization") token: String
    ): GetAllDetectionResponse

    @GET("detections/statistic")
    suspend fun getDetectionStat(): GetDetectionStatResponse

    @PATCH("detections")
    suspend fun updateDetections(
        @Header("Authorization") token: String,
        @Field("id") id: String,
        @Field("status") status: String,
        @Field("idUserLogin") idUserLogin: String,
    ): ErrorMessageResponse

    @Multipart
    @POST("detections")
    suspend fun addNewDetection(
        @Header("Authorization") token: String,
        @Part recordUrl: MultipartBody.Part,
        @Part("lat") lat: RequestBody,
        @Part("lon") lon: RequestBody,
        @Part("type") type: RequestBody,
        @Part("userId") userId: RequestBody,
        @Part("city") city: RequestBody
    ): ErrorMessageResponse

    @GET("detections/{id}")
    suspend fun getDetectionDetail(
        @Header("Authorization") token: String,
        @Path("id") id: String
    ): GetDetectionDetailResponse

    @DELETE("detections/{id}")
    fun deleteDetection(
        @Header("Authorization") token: String,
        @Path("id") id: String
    ): ErrorMessageResponse
}