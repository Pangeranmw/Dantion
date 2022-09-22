package com.bangkit.dantion.data.model

data class LoginResult(
    val id: String,
    val name: String,
    val address: String,
    val number: String,
    val parentNumber: String,
    val email: String,
    val role: String,
    val photo: String,
    val token: String,
)
