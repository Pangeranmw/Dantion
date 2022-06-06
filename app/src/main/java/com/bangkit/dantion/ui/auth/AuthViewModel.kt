package com.bangkit.dantion.ui.auth

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
class AuthViewModel @Inject constructor(private val dataStoreRepository: DataStoreRepository, private val authRepository: AuthRepository): ViewModel() {
    fun registerUser(registerBody: RegisterBody): LiveData<Result<ErrorMessageResponse>>{
        val registerResponse = MutableLiveData<Result<ErrorMessageResponse>>()
        viewModelScope.launch {
            authRepository.registerUser(registerBody).collect {
                registerResponse.postValue(it)
            }
        }
        return registerResponse
    }
    fun loginUser(loginBody: LoginBody): LiveData<Result<LoginResponse>>{
        val loginResponse = MutableLiveData<Result<LoginResponse>>()
        viewModelScope.launch {
            authRepository.loginUser(loginBody).collect {
                loginResponse.postValue(it)
            }
        }
        return loginResponse
    }
}