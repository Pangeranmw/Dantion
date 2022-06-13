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
import com.bangkit.dantion.data.source.DatabaseDataSource
import com.bangkit.dantion.data.source.DetectionDataSource
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
class DetectionRepository @Inject constructor (
    private val detectionDataSource: DetectionDataSource,
    private val appDatabase: AppDatabase,
    private val databaseDataSource: DatabaseDataSource
) {
    suspend fun getDetectionStat(): Flow<Result<GetDetectionStatResponse>> {
        return detectionDataSource.getDetectionStat().flowOn(Dispatchers.IO)
    }
    suspend fun getAllDetections(token: String): Flow<Result<GetAllDetectionResponse>> {
        return detectionDataSource.getAllDetections(token).flowOn(Dispatchers.IO)
    }
    suspend fun getDetectionDetail(token: String, id: String): Flow<Result<GetDetectionDetailResponse>> {
        return detectionDataSource.getDetectionDetail(token, id).flowOn(Dispatchers.IO)
    }
    suspend fun deleteDetection(token: String, id: String): Flow<Result<ErrorMessageResponse>> {
        return detectionDataSource.deleteDetection(token, id).flowOn(Dispatchers.IO)
    }
    suspend fun updateDetections(token: String, updateField: UpdateDetectionBody): Flow<Result<ErrorMessageResponse>> {
        return detectionDataSource.updateDetections(token, updateField).flowOn(Dispatchers.IO)
    }
    suspend fun addNewDetection(
        token: String,
        recordUrl: MultipartBody.Part,
        city: RequestBody,
        lat: RequestBody,
        lon: RequestBody,
        type: RequestBody,
        userId: RequestBody
    ): Flow<Result<ErrorMessageResponse>> {
        return detectionDataSource.addNewDetection(
            token = token,
            recordUrl = recordUrl,
            city = city,
            lat = lat,
            lon = lon,
            type = type,
            userId = userId,
        ).flowOn(Dispatchers.IO)
    }
    suspend fun insertDangerCase(detection: List<Detection>){
        databaseDataSource.insertDangerCase(detection).flowOn(Dispatchers.IO)
    }
    suspend fun deleteAllDangerCase(){
        databaseDataSource.deleteAllDangerCase().flowOn(Dispatchers.IO)
    }
    fun getAllDangerCase(): Flow<ArrayList<CaseEntity>> {
        return databaseDataSource.getAllDangerCase().flowOn(Dispatchers.IO)
    }
}