package com.bangkit.dantion.data.remote.user

data class RegisterBody(
    val name: String,
    val address: String,
    val number: String,
    val parentNumber: String,
    val email: String,
    val password: String,
)