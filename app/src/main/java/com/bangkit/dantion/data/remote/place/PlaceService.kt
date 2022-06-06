package com.bangkit.dantion.data.remote.place

import com.bangkit.dantion.data.remote.ErrorMessageResponse
import retrofit2.http.*

interface PlaceService {
    @GET("places")
    suspend fun getAllPlaces(
        @Header("Authorization") token: String
    ): GetAllPlaceResponse

    @PATCH("places")
    suspend fun updateDetections(
        @Header("Authorization") token: String,
        @Body updatePlaceBody: UpdatePlaceBody
    ): ErrorMessageResponse

    @Multipart
    @POST("places")
    suspend fun addNewPlace(
        @Header("Authorization") token: String,
        @Body addPlaceBody: AddPlaceBody
    ): ErrorMessageResponse

    @GET("places/{id}")
    fun getDetectionDetail(
        @Header("Authorization") token: String,
        @Path("id") id: String
    ): PlaceDetailResponse

    @DELETE("places/{id}")
    fun deleteDetection(
        @Header("Authorization") token: String,
        @Path("id") id: String
    ): ErrorMessageResponse
}