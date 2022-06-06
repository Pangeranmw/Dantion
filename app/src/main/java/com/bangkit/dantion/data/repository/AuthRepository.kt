package com.bangkit.dantion.data.repository

import com.bangkit.dantion.data.Result
import com.bangkit.dantion.data.remote.ErrorMessageResponse
import com.bangkit.dantion.data.remote.user.*
import com.bangkit.dantion.data.source.AuthDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepository @Inject constructor (
    private val authDataSource: AuthDataSource
) {
    suspend fun registerUser(registerBody: RegisterBody): Flow<Result<ErrorMessageResponse>> {
        return authDataSource.registerUser(registerBody).flowOn(Dispatchers.IO)
    }
    suspend fun loginUser(loginBody: LoginBody): Flow<Result<LoginResponse>> {
        return authDataSource.loginUser(loginBody).flowOn(Dispatchers.IO)
    }
}