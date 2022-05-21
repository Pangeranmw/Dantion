package com.bangkit.dantion.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.doublePreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "dantion_data_store")

class UserPreference @Inject constructor (@ApplicationContext context: Context) {

    private val appContext = context.applicationContext

    val login: Flow<Boolean?>
        get() = appContext.dataStore.data.map { preferences ->
            preferences[LOGIN_STATE]
        }

    val onBoarding: Flow<Boolean?>
        get() = appContext.dataStore.data.map { preferences ->
            preferences[ON_BOARDING_STATE]
        }

    suspend fun saveLogin() {
        appContext.dataStore.edit { preferences ->
            preferences[LOGIN_STATE] = true
        }
    }

    suspend fun saveOnBoarding() {
        appContext.dataStore.edit { preferences ->
            preferences[ON_BOARDING_STATE] = true
        }
    }

    suspend fun saveLocation(latitude: Double, longitude: Double) {
        appContext.dataStore.edit { preferences ->
            preferences[LAT_KEY] = latitude
            preferences[LON_KEY] = longitude
        }
    }

    suspend fun clear() {
        appContext.dataStore.edit { preferences ->
            preferences.clear()
        }
    }

    companion object {
        private val ON_BOARDING_STATE = booleanPreferencesKey("onBoardingState")
        private val LOGIN_STATE = booleanPreferencesKey("loginState")
        private val LAT_KEY = doublePreferencesKey("latitude")
        private val LON_KEY = doublePreferencesKey("longitude")
    }

}