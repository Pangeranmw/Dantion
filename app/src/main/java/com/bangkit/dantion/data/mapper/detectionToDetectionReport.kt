package com.bangkit.dantion.data.mapper

import android.content.Context
import com.bangkit.dantion.R
import com.bangkit.dantion.data.local.entity.CaseEntity
import com.bangkit.dantion.data.local.entity.DetectionReportEntity
import com.bangkit.dantion.data.model.Detection
import com.bangkit.dantion.getAddress
import com.bangkit.dantion.getCity

fun detectionToDetectionReport(detection: List<Detection>, context: Context): List<DetectionReportEntity> {
    return detection.map { det ->
        DetectionReportEntity(
            detectionId = det.id,
            address = getAddress(det.lat, det.lon, context) ?: context.getString(R.string.location_unknown),
            city = getCity(det.lat, det.lon, context),
            type = det.type,
            status = det.status,
            createdAt = det.updatedAt,
        )
    }
}