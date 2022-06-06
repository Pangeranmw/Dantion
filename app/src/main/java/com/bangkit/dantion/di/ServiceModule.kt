package com.bangkit.dantion.di

import com.bangkit.dantion.data.remote.detection.DetectionService
import com.bangkit.dantion.data.remote.place.PlaceService
import com.bangkit.dantion.data.remote.user.UserService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit

@Module
@InstallIn(SingletonComponent::class)
class ServiceModule {

    @Provides
    fun provideUserService(retrofit: Retrofit): UserService = retrofit.create(UserService::class.java)

    @Provides
    fun providePlaceService(retrofit: Retrofit): PlaceService = retrofit.create(PlaceService::class.java)

    @Provides
    fun provideDetectionService(retrofit: Retrofit): DetectionService = retrofit.create(DetectionService::class.java)

}