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

    suspend fun registerUser(registerField: RegisterField): Flow<Result<ErrorMessageResponse>> {
        return flow {
            try {
                emit(Result.Loading)
                val response = userService.registerUser(
                    name = registerField.name,
                    address = registerField.address,
                    password = registerField.password,
                    email = registerField.email,
                    parentNumber = registerField.parentNumber,
                    number = registerField.number
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

    suspend fun loginUser(loginField: LoginField): Flow<Result<LoginResponse>> {
        return flow {
            try {
                emit(Result.Loading)
                val response = userService.loginUser(
                    loginField.email,
                    loginField.password
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