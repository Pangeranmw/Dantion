package com.bangkit.dantion.data.remote.detection

data class UpdateDetectionBody(
    val id: String,
    val lat: Double,
    val lon: Double,
    val radius: Double,
    val type: String,
)
