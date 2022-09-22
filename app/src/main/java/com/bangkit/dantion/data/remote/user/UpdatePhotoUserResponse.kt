package com.bangkit.dantion.data.remote.user

import com.bangkit.dantion.data.model.User

data class UpdatePhotoUserResponse(
    val error: Boolean,
    val message: String,
    val photo: String,
)