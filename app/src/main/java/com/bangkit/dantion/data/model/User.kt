package com.bangkit.dantion.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class User(
    val id: String?=null,
    val name: String?=null,
    val address: String?=null,
    val number: String?=null,
    val parentNumber: String?=null,
    val email: String?=null,
    val password: String?=null,
    val role: String?=null,
    val photo: String?=null,
    val createdAt: String? = null,
    val updatedAt: String? = null,
): Parcelable
