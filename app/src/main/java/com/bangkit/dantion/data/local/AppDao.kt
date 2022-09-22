package com.bangkit.dantion.data.local

import androidx.lifecycle.LiveData
import androidx.room.*
import com.bangkit.dantion.data.local.entity.CaseEntity
import com.bangkit.dantion.data.local.entity.DetectionReportEntity
import com.bangkit.dantion.data.local.entity.MyDetectionReportEntity

@Dao
interface AppDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertDangerCase(dangerCase: List<CaseEntity>)

    @Query("SELECT * from danger_case")
    fun getAllDangerCase(): List<CaseEntity>

    @Query("DELETE FROM danger_case")
    suspend fun deleteAllDangerCase()

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertDetectionReport(detectionReport: List<DetectionReportEntity>)

    @Query("SELECT * from detection_report")
    fun getAllDetectionReport(): List<DetectionReportEntity>

    @Query("DELETE FROM detection_report")
    suspend fun deleteAllDetectionReport()

    @Query("UPDATE detection_report SET isRead = :isRead WHERE detectionId= :id")
    suspend fun updateReadDetectionReport(id: String, isRead: Boolean)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertMyDetectionReport(myDetectionReport: List<MyDetectionReportEntity>)

    @Query("SELECT * from my_detection_report")
    fun getAllMyDetectionReport(): List<MyDetectionReportEntity>

    @Query("UPDATE my_detection_report SET isRead = :isRead WHERE detectionId= :id")
    suspend fun updateReadMyDetectionReport(id: String, isRead: Boolean)

    @Query("DELETE FROM detection_report")
    suspend fun deleteAllMyDetectionReport()

    @Query("UPDATE my_detection_report SET isRead = 1")
    suspend fun readAllMyReport()

    @Query("UPDATE detection_report SET isRead = 1")
    suspend fun readAllReport()

    @Query("SELECT COUNT(*)+(SELECT COUNT(*) FROM my_detection_report WHERE isRead = 0) FROM detection_report WHERE isRead = 0")
    fun getUnreadNotification(): Int
}