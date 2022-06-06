package com.bangkit.dantion.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Place(
    val id: String,
    val lat: Float,
    val lon: Float,
    val radius: Float,
    val type: String,
    val createdAt: String,
    val updatedAt: String,
): Parcelable
