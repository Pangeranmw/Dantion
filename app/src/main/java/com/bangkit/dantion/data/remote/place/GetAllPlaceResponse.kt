package com.bangkit.dantion.data.remote.place

import com.bangkit.dantion.data.model.Place

data class GetAllPlaceResponse(
    val error: Boolean,
    val message: String,
    val places: List<Place>,
)
