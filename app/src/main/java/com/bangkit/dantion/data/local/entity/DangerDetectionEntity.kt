package com.bangkit.dantion.data.local.entity

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "dangerDetection")
data class DangerDetectionEntity(
    @PrimaryKey val dangerDetectionId: String,
    val uId: String,
    val latitude: Double,
    val longitude: Double,
    val record: String,
    val type: String,
    val status: String,
    val isValid: Boolean,
    val createdAt: String,
    val updatedAt: String,
): Parcelable
