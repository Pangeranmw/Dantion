package com.bangkit.dantion.data.remote.detection

import com.bangkit.dantion.data.model.Detection

data class GetDetectionDetailResponse(
    val error: Boolean,
    val message: String,
    val detection: Detection,
)