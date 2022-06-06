package com.bangkit.dantion.data.remote.place

data class AddPlaceBody(
    val lat: Float,
    val lon: Float,
    val radius: Float,
    val type: String,
)
