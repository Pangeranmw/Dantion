package com.bangkit.dantion.data.remote.user

import com.bangkit.dantion.data.model.User

data class GetDetailUserResponse(
    val error: Boolean,
    val message: String,
    val users: User,
)