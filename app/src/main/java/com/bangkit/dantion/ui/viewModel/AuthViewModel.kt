package com.bangkit.dantion.ui.viewModel

import androidx.lifecycle.*
import com.bangkit.dantion.data.remote.ErrorMessageResponse
import com.bangkit.dantion.data.remote.user.*
import com.bangkit.dantion.data.repository.AuthRepository
import com.bangkit.dantion.data.repository.DataStoreRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.bangkit.dantion.data.Result

@HiltViewModel
class AuthViewModel @Inject constructor(private val dataStoreRepository: DataStoreRepository, private val authRepository: AuthRepository): ViewModel() {
    fun registerUser(registerField: RegisterField): LiveData<Result<ErrorMessageResponse>>{
        val registerResponse = MutableLiveData<Result<ErrorMessageResponse>>()
        viewModelScope.launch {
            authRepository.registerUser(registerField).collect {
                registerResponse.postValue(it)
            }
        }
        return registerResponse
    }
    fun loginUser(loginField: LoginField): LiveData<Result<LoginResponse>>{
        val loginResponse = MutableLiveData<Result<LoginResponse>>()
        viewModelScope.launch {
            authRepository.loginUser(loginField).collect {
                loginResponse.postValue(it)
            }
        }
        return loginResponse
    }
}