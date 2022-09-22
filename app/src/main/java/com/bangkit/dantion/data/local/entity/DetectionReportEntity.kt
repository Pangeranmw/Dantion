package com.bangkit.dantion.data.local.entity

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "detection_report")
data class DetectionReportEntity(
    @PrimaryKey var detectionId: String,
    var createdAt: String,
    var address: String,
    var city: String,
    var type: String,
    var isRead: Boolean = false,
    var status: String
): Parcelable