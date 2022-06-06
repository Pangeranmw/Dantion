package com.bangkit.dantion.data.remote.user

data class UpdatePasswordBody(
    val id: String,
    val password: String,
    val newPassword: String
)
