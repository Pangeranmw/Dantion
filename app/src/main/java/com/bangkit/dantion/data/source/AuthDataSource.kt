package com.bangkit.dantion.data.source

import com.bangkit.dantion.data.Result
import com.bangkit.dantion.data.remote.ErrorMessageResponse
import com.bangkit.dantion.data.remote.user.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthDataSource @Inject constructor(private val userService: UserService) {

    suspend fun registerUser(registerBody: RegisterBody): Flow<Result<ErrorMessageResponse>> {
        return flow {
            try {
                emit(Result.Loading)
                val response = userService.registerUser(registerBody)
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

    suspend fun loginUser(loginBody: LoginBody): Flow<Result<LoginResponse>> {
        return flow {
            try {
                emit(Result.Loading)
                val response = userService.loginUser(loginBody)
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