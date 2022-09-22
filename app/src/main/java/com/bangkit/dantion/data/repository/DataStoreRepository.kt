package com.bangkit.dantion.data.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import com.bangkit.dantion.data.DataStoreAbstract
import com.bangkit.dantion.data.model.LoginResult
import com.bangkit.dantion.data.remote.user.RegisterField
import kotlinx.coroutines.flow.Flow
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
    override suspend fun saveIdUser(id: String) {
        context.datastore.edit { pref->
            pref[ID] = id
        }
    }
    override suspend fun logout(){
        context.datastore.edit {
            it.clear()
            it[ON_BOARDING] = true
        }
    }
    override suspend fun saveUser(user: LoginResult) {
        context.datastore.data.map{
            context.datastore.edit { pref ->
                pref[ID] = user.id
                pref[NAME] = user.name
                pref[EMAIL] = user.email
                pref[ADDRESS] = user.address
                pref[NUMBER] = user.number
                pref[PARENT_NUMBER] = user.parentNumber
                pref[ROLE] = user.role
                pref[PHOTO] = user.photo
            }
        }
    }
    override suspend fun saveToken(token: String) {
        context.datastore.edit { pref ->
            pref[TOKEN] = token
        }
    }
    override suspend fun saveRegister(user: RegisterField) {
        context.datastore.edit { pref ->
            pref[NAME] = user.name
            pref[ADDRESS] = user.address
            pref[NUMBER] = user.number
            pref[PARENT_NUMBER] = user.parentNumber
        }
    }
    override suspend fun saveLocation(latitude: Double, longitude: Double) {
        context.datastore.edit { pref->
            pref[LATITUDE] = latitude
            pref[LONGITUDE] = longitude
        }
    }

    override fun getLogin() = context.datastore.data.map{ it[LOGIN] }
    override fun getIdUser(): Flow<String> = context.datastore.data.map{ it[ID]?:"" }
    override fun getToken(): Flow<String> = context.datastore.data.map{ it[TOKEN]?:"" }
    override fun getUser() = context.datastore.data.map{ pref ->
        LoginResult(
            id = pref[ID]?:"",
            name = pref[NAME]?:"",
            email = pref[EMAIL]?:"",
            address = pref[ADDRESS]?:"",
            number = pref[NUMBER]?:"",
            parentNumber = pref[PARENT_NUMBER]?:"",
            role = pref[ROLE]?:"",
            photo = pref[PHOTO]?:"",
            token = pref[TOKEN]?:""
        )
    }
    override fun getRegister() = context.datastore.data.map{ pref ->
        RegisterField(
            name = pref[NAME]?:"",
            email = pref[EMAIL]?:"",
            address = pref[ADDRESS]?:"",
            number = pref[NUMBER]?:"",
            parentNumber = pref[PARENT_NUMBER]?:"",
            password = ""
        )
    }
    override fun getLatitude(): Flow<Double> = context.datastore.data.map{ it[LATITUDE]?:0.0 }
    override fun getLongitude(): Flow<Double> = context.datastore.data.map{ it[LONGITUDE]?:0.0 }

    companion object{
        val ON_BOARDING = booleanPreferencesKey("onBoarding")
        val LOGIN = booleanPreferencesKey("login")

        val ID = stringPreferencesKey("id")
        val ADDRESS = stringPreferencesKey("address")
        val NAME = stringPreferencesKey("name")
        val EMAIL = stringPreferencesKey("email")
        val NUMBER = stringPreferencesKey("number")
        val PARENT_NUMBER = stringPreferencesKey("parentNumber")
        val ROLE = stringPreferencesKey("role")
        val PHOTO = stringPreferencesKey("photo")
        val TOKEN = stringPreferencesKey("token")

        val LATITUDE = doublePreferencesKey("latitude")
        val LONGITUDE = doublePreferencesKey("longitude")
    }
}