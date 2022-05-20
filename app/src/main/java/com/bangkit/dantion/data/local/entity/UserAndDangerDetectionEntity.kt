package com.bangkit.dantion.data.local.entity

import androidx.room.Embedded
import androidx.room.Relation

data class UserAndDangerDetectionEntity(
    @Embedded val user: UserEntity,
    @Relation(
        parentColumn = "userId",
        entityColumn = "uId"
    )
    val dangerDetection: List<DangerDetectionEntity> ?= null
)