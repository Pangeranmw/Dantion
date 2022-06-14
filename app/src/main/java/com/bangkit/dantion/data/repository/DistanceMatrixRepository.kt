package com.bangkit.dantion.data.repository

import com.bangkit.dantion.data.Result
import com.bangkit.dantion.data.local.AppDatabase
import com.bangkit.dantion.data.local.entity.CaseEntity
import com.bangkit.dantion.data.mapper.detectionToCaseEntity
import com.bangkit.dantion.data.model.Detection
import com.bangkit.dantion.data.remote.ErrorMessageResponse
import com.bangkit.dantion.data.remote.detection.GetDetectionDetailResponse
import com.bangkit.dantion.data.remote.detection.GetAllDetectionResponse
import com.bangkit.dantion.data.remote.detection.GetDetectionStatResponse
import com.bangkit.dantion.data.remote.detection.UpdateDetectionBody
import com.bangkit.dantion.data.remote.distanceMatrix.GetMatrixResponse
import com.bangkit.dantion.data.source.DatabaseDataSource
import com.bangkit.dantion.data.source.DetectionDataSource
import com.bangkit.dantion.data.source.DistanceMatrixDataSource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DistanceMatrixRepository @Inject constructor (
    private val distanceMatrixDataSource: DistanceMatrixDataSource,
) {
    suspend fun getDistanceMatrix(origins: String, destination: String): Flow<Result<GetMatrixResponse>> {
        return distanceMatrixDataSource.getDistanceMatrix(origins, destination).flowOn(Dispatchers.IO)
    }
}