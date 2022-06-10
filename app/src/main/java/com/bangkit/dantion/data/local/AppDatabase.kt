package com.bangkit.dantion.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.bangkit.dantion.data.local.entity.DetectionReportEntity

@Database(
    entities = [
        DetectionReportEntity::class,
        DetectionReportEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun appDao(): AppDao
}