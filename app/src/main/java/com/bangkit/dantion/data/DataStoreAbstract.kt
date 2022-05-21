package com.bangkit.dantion.data

import com.bangkit.dantion.data.model.User
import kotlinx.coroutines.flow.Flow

interface DataStoreAbstract {
    suspend fun saveOnBoarding()
    fun getOnBoarding(): Flow<Boolean?>
    suspend fun saveLogin()
    fun getLogin(): Flow<Boolean?>
    suspend fun saveUser(user: User)
    fun getUser(): Flow<User?>
    suspend fun saveLocation(latitude: Double, longitude: Double)
    fun getLatitude(): Flow<Double?>
    fun getLongitude(): Flow<Double?>
}
