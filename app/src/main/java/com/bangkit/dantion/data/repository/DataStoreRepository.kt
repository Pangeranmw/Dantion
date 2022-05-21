package com.bangkit.dantion.data.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import com.bangkit.dantion.data.DataStoreAbstract
import com.bangkit.dantion.data.model.User
import kotlinx.coroutines.flow.map

const val DANTION_DATA_STORE = "dantionDataStore"

val Context.datastore : DataStore<Preferences> by preferencesDataStore(name = DANTION_DATA_STORE)

class DataStoreRepository(private val context: Context): DataStoreAbstract {

    override suspend fun saveOnBoarding() {
        context.datastore.edit { pref->
            pref[ON_BOARDING] = true
        }
    }

    override fun getOnBoarding() = context.datastore.data.map{ it[ON_BOARDING] }

    override suspend fun saveLogin() {
        context.datastore.edit { pref->
            pref[LOGIN] = true
        }
    }
    override suspend fun saveUser(user: User) {
        context.datastore.edit { pref ->
            pref[NAME] = user.name?:""
            pref[ADDRESS] = user.address?:""
            pref[NUMBER] = user.number?:""
            pref[PARENT_NUMBER] = user.parentNumber?:""
        }
    }

    override suspend fun saveLocation(latitude: Double, longitude: Double) {
        context.datastore.edit { pref->
            pref[LATITUDE] = latitude
            pref[LONGITUDE] = longitude
        }
    }

    override fun getLogin() = context.datastore.data.map{ it[LOGIN] }
    override fun getUser() = context.datastore.data.map{ pref ->
        User(
            name = pref[NAME],
            address = pref[ADDRESS],
            number = pref[NUMBER],
            parentNumber = pref[PARENT_NUMBER],
        )
    }

    override fun getLatitude() = context.datastore.data.map{ it[LATITUDE] }
    override fun getLongitude() = context.datastore.data.map{ it[LONGITUDE] }

    companion object{
        val ON_BOARDING = booleanPreferencesKey("onBoarding")
        val LOGIN = booleanPreferencesKey("login")
        val ADDRESS = stringPreferencesKey("address")
        val NAME = stringPreferencesKey("name")
        val NUMBER = stringPreferencesKey("number")
        val PARENT_NUMBER = stringPreferencesKey("parentNumber")
        val LATITUDE = doublePreferencesKey("latitude")
        val LONGITUDE = doublePreferencesKey("longitude")
    }
}