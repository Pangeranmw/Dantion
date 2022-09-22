package com.bangkit.dantion.data.source

import android.content.Context
import com.bangkit.dantion.data.local.AppDatabase
import com.bangkit.dantion.data.local.entity.CaseEntity
import com.bangkit.dantion.data.local.entity.DetectionReportEntity
import com.bangkit.dantion.data.local.entity.MyDetectionReportEntity
import com.bangkit.dantion.data.mapper.detectionToCaseEntity
import com.bangkit.dantion.data.mapper.detectionToDetectionReport
import com.bangkit.dantion.data.mapper.detectionToMyDetectionReport
import com.bangkit.dantion.data.model.Detection
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DatabaseDataSource @Inject constructor(private val appDatabase: AppDatabase) {

//    suspend fun insertDangerCase(detection: List<Detection>):Flow<Any>{
//        return flow{ emit(appDatabase.appDao().insertDangerCase(detectionToCaseEntity(detection))) }
//    }
    suspend fun insertDangerCase(detection: List<Detection>){
        CoroutineScope(Dispatchers.IO).launch{
            appDatabase.appDao().insertDangerCase(detectionToCaseEntity(detection))
        }
    }
    suspend fun deleteAllDangerCase(): Flow<Any>{
        return flow{ emit(appDatabase.appDao().deleteAllDangerCase()) }
    }
    fun getAllDangerCase(): Flow<ArrayList<CaseEntity>> {
        return flow{ emit(appDatabase.appDao().getAllDangerCase() as ArrayList<CaseEntity>) }
    }

    suspend fun insertDetectionReport(detection: List<Detection>, context: Context){
        CoroutineScope(Dispatchers.IO).launch{
            appDatabase.appDao().insertDetectionReport(detectionToDetectionReport(detection,context))
        }
//        return flow{ emit(appDatabase.appDao().insertDetectionReport(detectionToDetectionReport(detection,context))) }
    }
    suspend fun deleteAllDetectionReport(){
        CoroutineScope(Dispatchers.IO).launch{
            appDatabase.appDao().deleteAllDetectionReport()
        }
//        return flow{ emit(appDatabase.appDao().deleteAllDetectionReport()) }
    }
    suspend fun updateReadDetectionReport(id: String, isRead: Boolean) {
        CoroutineScope(Dispatchers.IO).launch{
            appDatabase.appDao().updateReadDetectionReport(id,isRead)
        }
//        return flow{ emit(appDatabase.appDao().updateReadDetectionReport(id, isRead)) }
    }
    suspend fun readAllReport(){
        CoroutineScope(Dispatchers.IO).launch{
            appDatabase.appDao().readAllReport()
        }
//        return flow{ emit(appDatabase.appDao().readAllReport()) }
    }
    fun getAllDetectionReport(): Flow<ArrayList<DetectionReportEntity>> {
        return flow{ emit(appDatabase.appDao().getAllDetectionReport() as ArrayList<DetectionReportEntity>) }
    }

    suspend fun insertMyDetectionReport(detection: List<Detection>, context: Context){
        CoroutineScope(Dispatchers.IO).launch{
            appDatabase.appDao().insertMyDetectionReport(detectionToMyDetectionReport(detection,context))
        }
//        return flow{ emit(appDatabase.appDao().insertMyDetectionReport(detectionToMyDetectionReport(detection,context))) }
    }
    suspend fun deleteAllMyDetectionReport(){
        CoroutineScope(Dispatchers.IO).launch{
            appDatabase.appDao().deleteAllMyDetectionReport()
        }
//        return flow{ emit(appDatabase.appDao().deleteAllMyDetectionReport()) }
    }
    suspend fun updateReadMyDetectionReport(id: String, isRead: Boolean){
        CoroutineScope(Dispatchers.IO).launch{
            appDatabase.appDao().updateReadMyDetectionReport(id,isRead)
        }
//        return flow{ emit(appDatabase.appDao().updateReadMyDetectionReport(id, isRead)) }
    }
    suspend fun readAllMyReport(){
        CoroutineScope(Dispatchers.IO).launch{
            appDatabase.appDao().readAllMyReport()
        }
//        return flow{ emit(appDatabase.appDao().readAllMyReport()) }
    }
    fun getAllMyDetectionReport(): Flow<ArrayList<MyDetectionReportEntity>> {
        return flow{ emit(appDatabase.appDao().getAllMyDetectionReport() as ArrayList<MyDetectionReportEntity>) }
    }
    fun getUnreadNotification(): Flow<Int> {
        return flow{ emit(appDatabase.appDao().getUnreadNotification()) }
    }
}