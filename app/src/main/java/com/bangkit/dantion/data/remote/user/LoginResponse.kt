package com.bangkit.dantion.data.remote.user

import com.bangkit.dantion.data.model.LoginResult

data class LoginResponse(
    val error: Boolean,
    val message: String,
    val loginResult: LoginResult
)