package com.bangkit.dantion.ui.auth

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.bangkit.dantion.data.repository.DataStoreRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(private val dataStoreRepository: DataStoreRepository): ViewModel() {
    fun saveLogin(){
        viewModelScope.launch(Dispatchers.IO){
            dataStoreRepository.saveLogin()
        }
    }
    fun getLogin(): LiveData<Boolean?> {
        return dataStoreRepository.getLogin().asLiveData()
    }
}