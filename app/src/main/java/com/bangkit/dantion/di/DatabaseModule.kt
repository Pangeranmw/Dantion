package com.bangkit.dantion.di

import android.content.Context
import androidx.room.Room
import com.bangkit.dantion.data.local.AppDatabase
import com.bangkit.dantion.data.local.AppDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext appContext: Context): AppDatabase {
        return Room.databaseBuilder(
            appContext,
            AppDatabase::class.java,
            "dantion.db"
        ).build()
    }

    @Provides
    fun provideRoomDao(database: AppDatabase): AppDao {
        return database.appDao()
    }
}