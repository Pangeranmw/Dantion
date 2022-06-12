package com.bangkit.dantion.data.local.entity

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "danger_case")
data class CaseEntity(
    @PrimaryKey val detectionId: String,
    val createdAt: String,
    val address: String,
    val city: String,
    val type: String,
): Parcelable