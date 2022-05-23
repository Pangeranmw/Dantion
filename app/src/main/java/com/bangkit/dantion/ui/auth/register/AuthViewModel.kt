package com.bangkit.dantion.ui.auth.register

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.bangkit.dantion.data.model.User
import com.bangkit.dantion.data.repository.DataStoreRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(private val dataStoreRepository: DataStoreRepository): ViewModel() {
    fun saveUser(user: User){
        viewModelScope.launch(Dispatchers.IO){
            dataStoreRepository.saveUser(user)
        }
    }
    fun getUser(): LiveData<User>{
        return dataStoreRepository.getUser().asLiveData()
    }
}