package com.bangkit.dantion.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.bangkit.dantion.data.local.entity.CaseEntity
import com.bangkit.dantion.data.local.entity.DetectionReportEntity
import com.bangkit.dantion.data.local.entity.MyDetectionReportEntity

@Database(
    entities = [
        DetectionReportEntity::class,
        MyDetectionReportEntity::class,
        CaseEntity::class,
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun appDao(): AppDao
}