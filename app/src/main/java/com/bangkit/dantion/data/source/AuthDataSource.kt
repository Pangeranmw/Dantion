package com.bangkit.dantion.data.source

import com.bangkit.dantion.data.Result
import com.bangkit.dantion.data.remote.auth.AuthBody
import com.bangkit.dantion.data.remote.auth.AuthResponse
import com.bangkit.dantion.data.remote.auth.AuthService
import com.bangkit.dantion.data.remote.auth.LoginBody
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.json.JSONObject
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthDataSource @Inject constructor(private val authService: AuthService) {

    suspend fun registerUser(authBody: AuthBody): Flow<Result<Response<AuthResponse>>> {
        return flow {
            try {
                emit(Result.Loading)
                val response = authService.registerUser(authBody)
                if (response.code() == 201) {
                    emit(Result.Success(response))
                } else if (response.code() == 400) {
                    val errorBody = JSONObject(response.errorBody()!!.string())
                    emit(Result.Error(errorBody.getString("message")))
                }
            } catch (ex: Exception) {
                emit(Result.Error(ex.message.toString()))
            }
        }
    }

    suspend fun loginUser(loginBody: LoginBody): Flow<Result<AuthResponse>> {
        return flow {
            try {
                emit(Result.Loading)
                val response = authService.loginUser(loginBody)
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