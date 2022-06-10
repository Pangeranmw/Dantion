package com.bangkit.dantion.data.local.entity

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "my_detection_report")
data class MyDetectionReportEntity(
    @PrimaryKey val detectionId: String,
    val address: String,
    val city: String,
    val type: String,
    val createdAt: String,
): Parcelable
