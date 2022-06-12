package com.bangkit.dantion.data.repository

import com.bangkit.dantion.data.Result
import com.bangkit.dantion.data.remote.ErrorMessageResponse
import com.bangkit.dantion.data.remote.user.UpdatePasswordBody
import com.bangkit.dantion.data.source.UserDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class UserRepository @Inject constructor (
    private val userDataSource: UserDataSource
) {
    suspend fun updatePassword(token: String, pass: UpdatePasswordBody): Flow<Result<ErrorMessageResponse>> {
        return userDataSource.updatePassword(token, pass).flowOn(Dispatchers.IO)
    }
}