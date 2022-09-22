package com.bangkit.dantion.data.source

import android.util.Log
import com.bangkit.dantion.data.Result
import com.bangkit.dantion.data.remote.ErrorMessageResponse
import com.bangkit.dantion.data.remote.detection.GetDetectionDetailResponse
import com.bangkit.dantion.data.remote.user.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject

class UserDataSource @Inject constructor(private val userService: UserService) {
    suspend fun getUserDetail(token: String, id: String): Flow<Result<GetDetailUserResponse>> {
        return flow {
            try {
                emit(Result.Loading)
                Log.d("getUserDetail", id)
                val response = userService.getUserDetail("Bearer $token", id)
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
    suspend fun updatePassword(token: String, pass: UpdatePasswordBody): Flow<Result<ErrorMessageResponse>> {
        return flow {
            try {
                emit(Result.Loading)
                val response = userService.updatePassword(
                    token = "Bearer $token",
                    id = pass.id,
                    password = pass.password,
                    newPassword = pass.newPassword
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
    suspend fun updateUser(token: String, user: UpdateUserBody): Flow<Result<ErrorMessageResponse>> {
        return flow {
            try {
                emit(Result.Loading)
                val response = userService.updateUser(
                    token = "Bearer $token",
                    id = user.id,
                    name = user.name,
                    address = user.address,
                    number = user.number,
                    parentNumber = user.parentNumber,
                    email = user.email
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
    suspend fun updateUserPhoto(
        token: String,
        id: String,
        photo: MultipartBody.Part,
    ): Flow<Result<UpdatePhotoUserResponse>> {
        return flow {
            try {
                emit(Result.Loading)
                val response = userService.updatePhoto(
                    token = "Bearer $token",
                    id = id,
                    photo = photo
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
}