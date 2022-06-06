package com.bangkit.dantion.data.remote.detection

import com.bangkit.dantion.data.model.Detection

data class GetAllDetectionResponse(
    val error: Boolean,
    val message: String,
    val detections: List<Detection>,
)
