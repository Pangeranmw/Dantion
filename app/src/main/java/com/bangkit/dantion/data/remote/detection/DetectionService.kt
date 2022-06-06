package com.bangkit.dantion.data.remote.detection

import com.bangkit.dantion.data.remote.ErrorMessageResponse
import com.bangkit.dantion.data.remote.place.AddPlaceBody
import com.bangkit.dantion.data.remote.place.PlaceDetailResponse
import com.bangkit.dantion.data.remote.place.UpdatePlaceBody
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*

interface DetectionService {

    @GET("detections")
    suspend fun getAllDetections(
        @Header("Authorization") token: String
    ): GetAllDetectionResponse

    @PATCH("detections")
    suspend fun updateDetections(
        @Header("Authorization") token: String,
        @Body updatePlaceBody: UpdatePlaceBody
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
    fun getDetectionDetail(
        @Header("Authorization") token: String,
        @Path("id") id: String
    ): PlaceDetailResponse

    @DELETE("detections/{id}")
    fun deleteDetection(
        @Header("Authorization") token: String,
        @Path("id") id: String
    ): ErrorMessageResponse
}