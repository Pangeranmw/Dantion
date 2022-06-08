package com.bangkit.dantion.data.remote.detection

import com.bangkit.dantion.data.model.DetectionStat

data class GetDetectionStatResponse(
    val error: Boolean,
    val message: String,
    val stat: DetectionStat,
)
