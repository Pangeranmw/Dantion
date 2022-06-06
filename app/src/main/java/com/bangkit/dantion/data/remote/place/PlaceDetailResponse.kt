package com.bangkit.dantion.data.remote.place

import com.bangkit.dantion.data.model.Place

data class PlaceDetailResponse(
    val error: Boolean,
    val message: String,
    val places: Place,
)
