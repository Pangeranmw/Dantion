package com.bangkit.dantion.data.remote.user

data class UpdateUserBody(
    val id: String,
    val name: String,
    val address: String,
    val number: String,
    val parentNumber: String,
    val email: String,
)
