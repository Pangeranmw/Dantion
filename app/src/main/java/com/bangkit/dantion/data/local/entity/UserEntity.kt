package com.bangkit.dantion.data.local.entity

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "user")
data class UserEntity(
    @PrimaryKey val userId: String,
    val name: String,
    val number: String,
    val parentNumber: String,
    val email: String,
    val password: String
): Parcelable
