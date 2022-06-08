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
    suspend fun registerUser(registerField: RegisterField): Flow<Result<ErrorMessageResponse>> {
        return authDataSource.registerUser(registerField).flowOn(Dispatchers.IO)
    }
    suspend fun loginUser(loginField: LoginField): Flow<Result<LoginResponse>> {
        return authDataSource.loginUser(loginField).flowOn(Dispatchers.IO)
    }
}