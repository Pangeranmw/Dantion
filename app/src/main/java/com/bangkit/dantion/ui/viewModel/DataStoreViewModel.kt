package com.bangkit.dantion.ui.viewModel

import androidx.lifecycle.*
import com.bangkit.dantion.data.remote.user.*
import com.bangkit.dantion.data.repository.DataStoreRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.bangkit.dantion.data.model.LoginResult

@HiltViewModel
class DataStoreViewModel @Inject constructor(private val dataStoreRepository: DataStoreRepository): ViewModel() {
    fun saveRegister(user: RegisterField){
        viewModelScope.launch(Dispatchers.IO){
            dataStoreRepository.saveRegister(user)
        }
    }
    fun getRegister(): LiveData<RegisterField>{
        return dataStoreRepository.getRegister().asLiveData()
    }
    fun getToken(): LiveData<String>{
        return dataStoreRepository.getToken().asLiveData()
    }
    fun saveToken(token: String){
        viewModelScope.launch(Dispatchers.IO){
            dataStoreRepository.saveToken(token)
        }
    }
    fun saveUser(user: LoginResult){
        viewModelScope.launch(Dispatchers.IO){
            dataStoreRepository.saveUser(user)
        }
    }
    fun getUser(): LiveData<LoginResult>{
        return dataStoreRepository.getUser().asLiveData()
    }
    fun getIdUser(): LiveData<String> {
        return dataStoreRepository.getIdUser().asLiveData()
    }
    fun logout(){
        viewModelScope.launch(Dispatchers.IO){
            dataStoreRepository.logout()
        }
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
    fun getLatitude(): LiveData<Double> {
        return dataStoreRepository.getLatitude().asLiveData()
    }
    fun getLongitude(): LiveData<Double> {
        return dataStoreRepository.getLongitude().asLiveData()
    }
}