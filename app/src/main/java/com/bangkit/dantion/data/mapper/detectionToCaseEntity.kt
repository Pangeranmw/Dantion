package com.bangkit.dantion.data.mapper

import com.bangkit.dantion.data.local.entity.CaseEntity
import com.bangkit.dantion.data.model.Detection

fun detectionToCaseEntity(detection: List<Detection>): List<CaseEntity> {
    return detection.map { det ->
        CaseEntity(
            lat = det.lat,
            lon = det.lon,
            address = det.address,
            city = det.city,
            caseId = det.id,
            userId = det.userId,
            name = det.name,
            number = det.number,
            photo = det.photo,
            parentNumber = det.parentNumber,
            recordUrl = det.recordUrl,
            status = det.status,
            type = det.type,
            updatedAt = det.updatedAt,
            validatorName = det.validatorName,
            validatorPhoto = det.validatorPhoto
        )
    }
}