package com.bangkit.dantion.data.local

import android.os.Parcelable
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Relation
import kotlinx.parcelize.Parcelize

@Entity
@Parcelize
data class User(
    @PrimaryKey
    var userId: String,
    var name: String,
    var number: String,
    var parentNumber: String,
    var email: String,
    var password: String
): Parcelable

@Entity
@Parcelize
data class DangerDetection(
    @PrimaryKey
    var dangerDetectionId: String,
    var latitude: Double,
    var longitude: Double,
    var rekaman: String,
    var type: String,
    var status: String,
    var isValid: Boolean,
    var createdAt: String,
    var uId: String,
): Parcelable

data class UserAndDangerDetection(
    @Embedded
    val user: User,

    @Relation(
        parentColumn = "uId",
        entityColumn = "userId"
    )
    val dangerDetection: DangerDetection? = null
)

@Entity
@Parcelize
data class DangerPlace(
    @PrimaryKey
    var dangerPlaceId: String,
    var radius: Double,
    var latitude: Double,
    var longitude: Double,
    var type: String,
    var createdAt: String,
): Parcelable