package com.bangkit.dantion.data.local

import androidx.lifecycle.LiveData
import androidx.room.*
import com.bangkit.dantion.data.local.entity.DetectionReportEntity
import com.bangkit.dantion.data.local.entity.MyDetectionReportEntity

@Dao
interface AppDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertDetectionReport(detectionReport: DetectionReportEntity)

    @Query("SELECT * from detection_report")
    fun getAllDetectionReport(): LiveData<List<DetectionReportEntity>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertMyDetectionReport(myDetectionReport: MyDetectionReportEntity)

    @Query("SELECT * from my_detection_report")
    fun getAllMyDetectionReport(): LiveData<List<MyDetectionReportEntity>>
}