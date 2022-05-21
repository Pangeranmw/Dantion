package com.bangkit.dantion.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.bangkit.dantion.data.local.entity.DangerDetectionEntity
import com.bangkit.dantion.data.local.entity.DangerPlaceEntity
import com.bangkit.dantion.data.local.entity.UserEntity

/**
 * SQLite Database for storing the logs.
 */
@Database(
    entities = [UserEntity::class,
        DangerDetectionEntity::class,
        DangerPlaceEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun appDao(): AppDao
}