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

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertMyDetectionReport(myDetectionReport: List<MyDetectionReportEntity>)

    @Query("SELECT * from my_detection_report")
    fun getAllMyDetectionReport(): List<MyDetectionReportEntity>
}