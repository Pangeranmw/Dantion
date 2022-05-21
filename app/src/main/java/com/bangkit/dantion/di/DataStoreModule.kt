package com.bangkit.dantion.di

import android.content.Context
import com.bangkit.dantion.data.repository.DataStoreRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@InstallIn(SingletonComponent::class)
@Module
object DataStoreModule {
    @Singleton
    @Provides
    fun provideDataStoreRepository(@ApplicationContext context: Context) = DataStoreRepository(context)
}