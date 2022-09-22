package com.bangkit.dantion.data.mapper

import com.bangkit.dantion.data.local.entity.CaseEntity
import com.bangkit.dantion.data.model.Detection

fun caseEntityToDetection(caseEntity: CaseEntity): Detection {
    return Detection(
            userId = caseEntity.userId,
            lat = caseEntity.lat,
            lon = caseEntity.lon,
            address = caseEntity.address,
            city = caseEntity.city,
            id = caseEntity.caseId,
            name = caseEntity.name,
            number = caseEntity.number,
            photo = caseEntity.photo,
            parentNumber = caseEntity.parentNumber,
            recordUrl = caseEntity.recordUrl,
            status = caseEntity.status,
            type = caseEntity.type,
            updatedAt = caseEntity.updatedAt,
            validatorName = caseEntity.validatorName?: "",
            validatorPhoto = caseEntity.validatorPhoto?: ""
        )
}