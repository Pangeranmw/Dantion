package com.bangkit.dantion.data.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
data class DangerDetection(
    val dangerDetectionId: String,
    val latitude: Double,
    val longitude: Double,
    val record: String,
    val type: String,
    val status: String,
    val isValid: Boolean,
    val createdAt: String,
    val updatedAt: String,
    val uId: String,
): Parcelable
