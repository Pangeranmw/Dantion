package com.bangkit.dantion.data.local.entity

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "my_detection_report")
data class MyDetectionReportEntity(
    @PrimaryKey var detectionId: String,
    var address: String,
    var city: String,
    var type: String,
    var createdAt: String,
): Parcelable