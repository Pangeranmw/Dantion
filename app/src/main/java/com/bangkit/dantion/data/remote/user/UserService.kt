package com.bangkit.dantion.data.remote.user

import com.bangkit.dantion.data.remote.ErrorMessageResponse
import dagger.Provides
import okhttp3.MultipartBody
import retrofit2.http.*

interface UserService {
    @POST("users/register")
    suspend fun registerUser(
        @Body registerBody: RegisterBody
    ): ErrorMessageResponse

    @POST("users/login")
    suspend fun loginUser(
        @Body loginBody: LoginBody
    ): LoginResponse

    @GET("users")
    suspend fun getAllUsers(
        @Header("Authorization") token: String,
        @Query("id") id: String // id admin
    ): GetAllUserResponse

    @PATCH("users")
    suspend fun updateDetections(
        @Header("Authorization") token: String,
        @Body updateUserBody: UpdateUserBody
    ): ErrorMessageResponse

    @PATCH("users/password")
    suspend fun updatePassword(
        @Header("Authorization") token: String,
        @Body updatePasswordBody: UpdatePasswordBody
    ): ErrorMessageResponse

    @Multipart
    @PATCH("users/photo")
    suspend fun updatePhoto(
        @Header("Authorization") token: String,
        @Query("id") id: String, // id user
        @Part photo: MultipartBody.Part,
    ): ErrorMessageResponse

    @GET("users/{id}")
    fun getUserDetail(
        @Header("Authorization") token: String,
        @Path("id") id: String
    ): GetDetailUserResponse
}