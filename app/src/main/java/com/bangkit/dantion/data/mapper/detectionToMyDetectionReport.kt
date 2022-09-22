package com.bangkit.dantion.data.mapper

import android.content.Context
import com.bangkit.dantion.R
import com.bangkit.dantion.data.local.entity.CaseEntity
import com.bangkit.dantion.data.local.entity.DetectionReportEntity
import com.bangkit.dantion.data.local.entity.MyDetectionReportEntity
import com.bangkit.dantion.data.model.Detection
import com.bangkit.dantion.getAddress
import com.bangkit.dantion.getCity

fun detectionToMyDetectionReport(detection: List<Detection>, context: Context): List<MyDetectionReportEntity> {
    return detection.map { det ->
        MyDetectionReportEntity(
            detectionId = det.id,
            address = getAddress(det.lat, det.lon, context)?: context.getString(R.string.location_unknown),
            city = getCity(det.lat, det.lon, context),
            status = det.status,
            type = det.type,
            createdAt = det.updatedAt,
        )
    }
}