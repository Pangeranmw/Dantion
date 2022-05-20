package com.bangkit.dantion.data.local.entity

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "dangerPlace")
data class DangerPlaceEntity(
    @PrimaryKey val dangerPlaceId: String,
    val radius: Double,
    val latitude: Double,
    val longitude: Double,
    val type: String,
    val createdAt: String,
    val updatedAt: String,
): Parcelable
