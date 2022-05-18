package com.bangkit.dantion.data.local

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface RoomDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertUser(user: User)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertDangerDetection(dangerDetection: DangerDetection)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertDangerPlace(dangerPlace: DangerPlace)

    @Query("SELECT * from user")
    fun getAllUser(): LiveData<List<User>>

    @Transaction
    @Query("SELECT * from dangerDetection")
    fun getAllDangerDetection(): LiveData<List<UserAndDangerDetection>>

}