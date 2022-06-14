package com.bangkit.dantion.data.remote.distanceMatrix

import com.bangkit.dantion.BuildConfig
import retrofit2.http.*

interface DistanceMatrixService {
    @GET("json")
    suspend fun getDistanceMatrix(
        @Query("key") key: String = BuildConfig.DM_MAPS_KEY,
        @Query("origins") origins: String,
        @Query("destinations") destinations: String,
    ): GetMatrixResponse
}