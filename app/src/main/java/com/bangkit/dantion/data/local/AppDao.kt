package com.bangkit.dantion.data.local

import androidx.lifecycle.LiveData
import androidx.room.*
import com.bangkit.dantion.data.local.entity.DangerDetectionEntity
import com.bangkit.dantion.data.local.entity.DangerPlaceEntity
import com.bangkit.dantion.data.local.entity.UserAndDangerDetectionEntity
import com.bangkit.dantion.data.local.entity.UserEntity

@Dao
interface AppDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertUser(user: UserEntity)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertDangerDetection(dangerDetection: DangerDetectionEntity)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertDangerPlace(dangerPlace: DangerPlaceEntity)

    @Query("SELECT * from user")
    fun getAllUser(): LiveData<List<UserEntity>>

    @Transaction
    @Query("SELECT * from user")
    fun getAllDangerDetection(): LiveData<List<UserAndDangerDetectionEntity>>

}