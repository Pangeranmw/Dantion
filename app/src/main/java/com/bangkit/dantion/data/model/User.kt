package com.bangkit.dantion.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class User(
    val userId: String,
    val name: String,
    val number: String,
    val parentNumber: String,
    val email: String,
    val password: String,
    val photoUrl: String,
    val createdAt: String,
    val updatedAt: String,
): Parcelable
