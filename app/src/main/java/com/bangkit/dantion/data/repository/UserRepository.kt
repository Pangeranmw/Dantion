package com.bangkit.dantion.data.repository

import com.bangkit.dantion.data.Result
import com.bangkit.dantion.data.remote.ErrorMessageResponse
import com.bangkit.dantion.data.remote.detection.GetDetectionDetailResponse
import com.bangkit.dantion.data.remote.user.GetDetailUserResponse
import com.bangkit.dantion.data.remote.user.UpdatePasswordBody
import com.bangkit.dantion.data.remote.user.UpdatePhotoUserResponse
import com.bangkit.dantion.data.remote.user.UpdateUserBody
import com.bangkit.dantion.data.source.UserDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import okhttp3.MultipartBody
import javax.inject.Inject

class UserRepository @Inject constructor (
    private val userDataSource: UserDataSource
) {
    suspend fun updatePassword(token: String, pass: UpdatePasswordBody): Flow<Result<ErrorMessageResponse>> {
        return userDataSource.updatePassword(token, pass).flowOn(Dispatchers.IO)
    }
    suspend fun updateUser(token: String, user: UpdateUserBody): Flow<Result<ErrorMessageResponse>> {
        return userDataSource.updateUser(token, user).flowOn(Dispatchers.IO)
    }
    suspend fun updateUserPhoto(token: String, id: String, photo: MultipartBody.Part): Flow<Result<UpdatePhotoUserResponse>> {
        return userDataSource.updateUserPhoto(token, id, photo).flowOn(Dispatchers.IO)
    }
    suspend fun getUserDetail(token: String, id: String): Flow<Result<GetDetailUserResponse>> {
        return userDataSource.getUserDetail(token, id).flowOn(Dispatchers.IO)
    }
}