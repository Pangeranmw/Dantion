package com.bangkit.dantion.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class DangerPlace(
    val dangerPlaceId: String,
    val radius: Double,
    val latitude: Double,
    val longitude: Double,
    val type: String,
    val createdAt: String,
    val updatedAt: String,
): Parcelable
