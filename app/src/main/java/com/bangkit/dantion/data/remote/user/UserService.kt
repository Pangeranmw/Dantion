package com.bangkit.dantion.data.remote.user

import com.bangkit.dantion.data.remote.ErrorMessageResponse
import okhttp3.MultipartBody
import retrofit2.http.*

interface UserService {
    @FormUrlEncoded
    @POST("users/register")
    suspend fun registerUser(
        @Field("name") name: String,
        @Field("address") address: String,
        @Field("number") number: String,
        @Field("parentNumber") parentNumber: String,
        @Field("email") email: String,
        @Field("password") password: String,
    ): ErrorMessageResponse

    @FormUrlEncoded
    @POST("users/login")
    suspend fun loginUser(
        @Field("email") email: String,
        @Field("password") password: String,
    ): LoginResponse

    @GET("users")
    suspend fun getAllUsers(
        @Header("Authorization") token: String,
        @Query("id") id: String // id admin
    ): GetAllUserResponse

    @FormUrlEncoded
    @PATCH("users")
    suspend fun updateDetections(
        @Header("Authorization") token: String,
        @Field("id") id: String,
        @Field("name") name: String,
        @Field("address") address: String,
        @Field("number") number: String,
        @Field("parentNumber") parentNumber: String,
        @Field("email") email: String,
    ): ErrorMessageResponse

    @FormUrlEncoded
    @PATCH("users/password")
    suspend fun updatePassword(
        @Header("Authorization") token: String,
        @Field("id") id: String,
        @Field("password") password: String,
        @Field("newPassword") newPassword: String,
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