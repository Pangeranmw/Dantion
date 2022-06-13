package com.bangkit.dantion.data.source

import com.bangkit.dantion.data.Result
import com.bangkit.dantion.data.local.AppDatabase
import com.bangkit.dantion.data.local.entity.CaseEntity
import com.bangkit.dantion.data.mapper.detectionToCaseEntity
import com.bangkit.dantion.data.model.Detection
import com.bangkit.dantion.data.remote.ErrorMessageResponse
import com.bangkit.dantion.data.remote.user.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DatabaseDataSource @Inject constructor(private val appDatabase: AppDatabase) {

    suspend fun insertDangerCase(detection: List<Detection>):Flow<Any>{
        return flow{ emit(appDatabase.appDao().insertDangerCase(detectionToCaseEntity(detection))) }
    }
    suspend fun deleteAllDangerCase(): Flow<Any>{
        return flow{ emit(appDatabase.appDao().deleteAllDangerCase()) }
    }
    fun getAllDangerCase(): Flow<ArrayList<CaseEntity>> {
        return flow{ emit(appDatabase.appDao().getAllDangerCase() as ArrayList<CaseEntity>) }
    }
}