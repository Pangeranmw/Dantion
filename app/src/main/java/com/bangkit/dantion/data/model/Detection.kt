package com.bangkit.dantion.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Detection(
    val lat: Double,
    val lon: Double,
    val recordUrl: String,
    val type: String,
    val status: String,
    val city: String,
    val updatedAt: String,
    val name: String,
    val address: String,
    val number: String,
    val parentNumber: String,
    val photo: String,
    val validatorName: String,
    val validatorPhoto: String,
): Parcelable
