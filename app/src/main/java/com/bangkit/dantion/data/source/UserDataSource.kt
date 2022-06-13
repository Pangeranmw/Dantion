package com.bangkit.dantion.data.source

import com.bangkit.dantion.data.Result
import com.bangkit.dantion.data.remote.ErrorMessageResponse
import com.bangkit.dantion.data.remote.user.UpdatePasswordBody
import com.bangkit.dantion.data.remote.user.UserService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class UserDataSource @Inject constructor(private val userService: UserService) {
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
}