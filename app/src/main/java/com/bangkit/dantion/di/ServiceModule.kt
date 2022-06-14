package com.bangkit.dantion.di

import com.bangkit.dantion.data.remote.detection.DetectionService
import com.bangkit.dantion.data.remote.distanceMatrix.DistanceMatrixService
import com.bangkit.dantion.data.remote.place.PlaceService
import com.bangkit.dantion.data.remote.user.UserService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Named

@Module
@InstallIn(SingletonComponent::class)
class ServiceModule {

    @Provides
    fun provideUserService(@Named("provideRetrofitBase") retrofit: Retrofit): UserService = retrofit.create(UserService::class.java)

    @Provides
    fun providePlaceService(@Named("provideRetrofitBase") retrofit: Retrofit): PlaceService = retrofit.create(PlaceService::class.java)

    @Provides
    fun provideDetectionService(@Named("provideRetrofitBase") retrofit: Retrofit): DetectionService = retrofit.create(DetectionService::class.java)

    @Provides
    fun provideDMService(@Named("provideRetrofitDM") retrofit: Retrofit): DistanceMatrixService = retrofit.create(DistanceMatrixService::class.java)

}