package com.bangkit.dantion.data.remote.user

import com.bangkit.dantion.data.model.User

data class GetAllUserResponse(
    val error: Boolean,
    val message: String,
    val users: List<User>,
)