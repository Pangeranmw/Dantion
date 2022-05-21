package com.bangkit.dantion.data.repository

import com.bangkit.dantion.data.Result
import com.bangkit.dantion.data.remote.auth.AuthBody
import com.bangkit.dantion.data.remote.auth.AuthResponse
import com.bangkit.dantion.data.remote.auth.LoginBody
import com.bangkit.dantion.data.source.AuthDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepository @Inject constructor (
    private val authDataSource: AuthDataSource
) {
    suspend fun registerUser(authBody: AuthBody): Flow<Result<Response<AuthResponse>>> {
        return authDataSource.registerUser(authBody).flowOn(Dispatchers.IO)
    }

    suspend fun loginUser(loginBody: LoginBody): Flow<Result<AuthResponse>> {
        return authDataSource.loginUser(loginBody).flowOn(Dispatchers.IO)
    }
}