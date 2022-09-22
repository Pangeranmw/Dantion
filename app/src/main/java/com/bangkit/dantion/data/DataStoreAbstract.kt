package com.bangkit.dantion.data

import com.bangkit.dantion.data.model.LoginResult
import com.bangkit.dantion.data.remote.user.RegisterField
import kotlinx.coroutines.flow.Flow

interface DataStoreAbstract {
    suspend fun saveOnBoarding()
    fun getOnBoarding(): Flow<Boolean?>
    suspend fun saveLogin()
    suspend fun logout()
    fun getLogin(): Flow<Boolean?>
    fun getToken(): Flow<String>
    fun getIdUser(): Flow<String>
    suspend fun saveToken(token: String)
    suspend fun saveUser(user: LoginResult)
    suspend fun saveIdUser(idUser: String)
    fun getUser(): Flow<LoginResult>
    suspend fun saveRegister(user: RegisterField)
    fun getRegister(): Flow<RegisterField>
    suspend fun saveLocation(latitude: Double, longitude: Double)
    fun getLatitude(): Flow<Double>
    fun getLongitude(): Flow<Double>
}
