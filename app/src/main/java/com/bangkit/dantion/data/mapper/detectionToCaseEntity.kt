package com.bangkit.dantion.data.mapper

import com.bangkit.dantion.data.local.entity.CaseEntity
import com.bangkit.dantion.data.model.Detection

fun detectionToCaseEntity(detection: List<Detection>): List<CaseEntity> {
    return detection.map { case ->
        CaseEntity(
            lat = case.lat,
            lon = case.lon,
            address = case.address,
            city = case.city,
            name = case.name,
            number = case.number,
            photo = case.photo,
            parentNumber = case.parentNumber,
            recordUrl = case.recordUrl,
            status = case.status,
            type = case.type,
            updatedAt = case.updatedAt,
            validatorName = case.validatorName,
            validatorPhoto = case.validatorPhoto
        )
    }
}