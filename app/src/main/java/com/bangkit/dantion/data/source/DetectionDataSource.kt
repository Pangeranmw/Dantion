package com.bangkit.dantion.data.source

import android.util.Log
import com.bangkit.dantion.data.Result
import com.bangkit.dantion.data.remote.ErrorMessageResponse
import com.bangkit.dantion.data.remote.detection.*
import com.bangkit.dantion.formatTo
import com.bangkit.dantion.toDate
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DetectionDataSource @Inject constructor(private val detectionService: DetectionService ) {

    suspend fun getDetectionStat(): Flow<Result<GetDetectionStatResponse>> {
        return flow {
            try {
                emit(Result.Loading)
                val response = detectionService.getDetectionStat()
                if (!response.error) {
                    emit(Result.Success(response))
                } else {
                    emit(Result.Error(response.message))
                }
            } catch (ex: Exception) {
                emit(Result.Error(ex.message.toString()))
            }
        }
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
        return flow {
            try {
                emit(Result.Loading)
                val response = detectionService.addNewDetection(
                    token = "Bearer $token",
                    recordUrl = recordUrl,
                    city = city,
                    lat = lat,
                    lon = lon,
                    type = type,
                    userId = userId,
                )
                if (!response.error) {
                    emit(Result.Success(response))
                } else {
                    emit(Result.Error(response.message))
                }
            } catch (ex: Exception) {
                emit(Result.Error(ex.message.toString()))
            }
        }
    }

    suspend fun getAllDetections(token: String): Flow<Result<GetAllDetectionResponse>> {
        return flow {
            try {
                emit(Result.Loading)
                val response = detectionService.getAllDetections("Bearer $token")
                if (!response.error) {
                    emit(Result.Success(response))
                } else {
                    emit(Result.Error(response.message))
                }
            } catch (ex: Exception) {
                emit(Result.Error(ex.message.toString()))
            }
        }
    }
    suspend fun getMyDetections(token: String, id: String): Flow<Result<GetAllDetectionResponse>> {
        return flow {
            try {
                emit(Result.Loading)
                val response = detectionService.getAllDetections("Bearer $token")
                if (!response.error) {
                    val today = SimpleDateFormat("yyyy-MM-dd").format(Date())
                    val myReport = response.detections.filter{
                        (id == it.userId) &&
                        if(it.updatedAt.contains("Z")) {
                            today.contains(it.updatedAt.toDate("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").formatTo("yyyy-MM-dd"), true)
                        }else today.contains(it.updatedAt.toDate("yyyy-MM-dd HH:mm:ss.SSSX").formatTo("yyyy-MM-dd"), true)
                    }
                    val newResponse = GetAllDetectionResponse(response.error, response.message, myReport)
                    emit(Result.Success(newResponse))
                } else {
                    emit(Result.Error(response.message))
                }
            } catch (ex: Exception) {
                emit(Result.Error(ex.message.toString()))
            }
        }
    }
    suspend fun getTodayDetections(token: String, id: String, city: String): Flow<Result<GetAllDetectionResponse>> {
        return flow {
            try {
                emit(Result.Loading)
                val response = detectionService.getAllDetections("Bearer $token")
                if (!response.error) {
                    val today = SimpleDateFormat("yyyy-MM-dd").format(Date())
                    val todayReport = response.detections.filter{
                        city.toString().contains(it.city, true) &&
                                (id.toString() != it.userId) &&
                                // get detection that only have valid or complete status
                                (it.status == "valid" || it.status == "selesai") &&
                                // get today detection report
                                if(it.updatedAt.contains("Z")) {
                                    today.contains(it.updatedAt.toDate("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").formatTo("yyyy-MM-dd"), true)
                                }else today.contains(it.updatedAt.toDate("yyyy-MM-dd HH:mm:ss.SSSX").formatTo("yyyy-MM-dd"), true)
                    }
                    val newResponse = GetAllDetectionResponse(response.error, response.message, todayReport)
                    emit(Result.Success(newResponse))
                } else {
                    emit(Result.Error(response.message))
                }
            } catch (ex: Exception) {
                emit(Result.Error(ex.message.toString()))
            }
        }
    }
    suspend fun getDetectionDetail(token: String, id: String): Flow<Result<GetDetectionDetailResponse>> {
        return flow {
            try {
                emit(Result.Loading)
                val response = detectionService.getDetectionDetail("Bearer $token", id)
                if (!response.error) {
                    emit(Result.Success(response))
                } else {
                    emit(Result.Error(response.message))
                }
            } catch (ex: Exception) {
                emit(Result.Error(ex.message.toString()))
            }
        }
    }

    suspend fun updateDetections(token: String, updateField: UpdateDetectionBody): Flow<Result<ErrorMessageResponse>> {
        return flow {
            try {
                emit(Result.Loading)
                val response = detectionService.updateDetections("Bearer $token",
                    updateField.id, updateField.status, updateField.idUserLogin)
                if (!response.error) {
                    emit(Result.Success(response))
                } else {
                    emit(Result.Error(response.message))
                }
            } catch (ex: Exception) {
                emit(Result.Error(ex.message.toString()))
            }
        }
    }

    suspend fun deleteDetection(token: String, id: String): Flow<Result<ErrorMessageResponse>> {
        return flow {
            try {
                emit(Result.Loading)
                val response = detectionService.deleteDetection("Bearer $token", id)
                if (!response.error) {
                    emit(Result.Success(response))
                } else {
                    emit(Result.Error(response.message))
                }
            } catch (ex: Exception) {
                emit(Result.Error(ex.message.toString()))
            }
        }
    }
}