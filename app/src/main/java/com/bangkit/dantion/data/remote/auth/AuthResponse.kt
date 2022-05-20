package com.bangkit.dantion.data.remote.auth


import com.bangkit.dantion.data.model.User
import com.google.gson.annotations.SerializedName

data class AuthResponse(
    @SerializedName("error")
    val error: Boolean,
    @SerializedName("message")
    val message: String,
    @SerializedName("loginUser")
    val loginUser: User
)