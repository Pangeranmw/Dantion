package com.bangkit.dantion.data.local.entity

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "danger_case")
data class CaseEntity(
    @PrimaryKey(autoGenerate = true) var caseId: Int=0,
    var lat: Double,
    var lon: Double,
    var recordUrl: String,
    var type: String,
    var status: String,
    var city: String,
    var updatedAt: String,
    var name: String,
    var address: String,
    var number: String,
    var parentNumber: String,
    var photo: String,
    var validatorName: String ?= null,
    var validatorPhoto: String  ?= null,
): Parcelable