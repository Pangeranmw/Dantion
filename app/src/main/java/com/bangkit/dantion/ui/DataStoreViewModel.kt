package com.bangkit.dantion.ui

import androidx.lifecycle.*
import com.bangkit.dantion.data.remote.ErrorMessageResponse
import com.bangkit.dantion.data.remote.user.*
import com.bangkit.dantion.data.repository.AuthRepository
import com.bangkit.dantion.data.repository.DataStoreRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.bangkit.dantion.data.Result
import com.bangkit.dantion.data.model.LoginResult
import com.bangkit.dantion.data.model.User

@HiltViewModel
class DataStoreViewModel @Inject constructor(private val dataStoreRepository: DataStoreRepository): ViewModel() {
    fun saveUser(user: LoginResult){
        viewModelScope.launch(Dispatchers.IO){
            dataStoreRepository.saveUser(user)
        }
    }
    fun getUser(): LiveData<LoginResult>{
        return dataStoreRepository.getUser().asLiveData()
    }
    fun saveLogin(){
        viewModelScope.launch(Dispatchers.IO){
            dataStoreRepository.saveLogin()
        }
    }
    fun getLogin(): LiveData<Boolean?> {
        return dataStoreRepository.getLogin().asLiveData()
    }
    fun saveOnBoarding(){
        viewModelScope.launch(Dispatchers.IO){
            dataStoreRepository.saveOnBoarding()
        }
    }
    fun getOnBoarding(): LiveData<Boolean?>{
        return dataStoreRepository.getOnBoarding().asLiveData()
    }
    fun saveLocation(lat: Double, lon: Double){
        viewModelScope.launch(Dispatchers.IO){
            dataStoreRepository.saveLocation(lat, lon)
        }
    }
}