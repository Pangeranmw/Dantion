package com.bangkit.dantion.data.source

import com.bangkit.dantion.data.Result
import com.bangkit.dantion.data.remote.ErrorMessageResponse
import com.bangkit.dantion.data.remote.detection.*
import com.bangkit.dantion.data.remote.distanceMatrix.DistanceMatrixService
import com.bangkit.dantion.data.remote.distanceMatrix.GetMatrixResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject
import javax.inject.Named
import javax.inject.Singleton

@Singleton
class DistanceMatrixDataSource @Inject constructor(private val distanceMatrixService: DistanceMatrixService) {

    suspend fun getDistanceMatrix(origins: String, destination: String): Flow<Result<GetMatrixResponse>> {
        return flow {
            try {
                emit(Result.Loading)
                val response = distanceMatrixService.getDistanceMatrix(origins=origins, destinations=destination)
                if (response.status == "OK") {
                    emit(Result.Success(response))
                } else {
                    emit(Result.Error(response.error_message))
                }
            } catch (ex: Exception) {
                emit(Result.Error(ex.message.toString()))
            }
        }
    }
}