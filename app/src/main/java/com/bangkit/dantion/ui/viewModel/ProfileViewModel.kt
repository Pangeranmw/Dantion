package com.bangkit.dantion.ui.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bangkit.dantion.data.Result
import com.bangkit.dantion.data.remote.ErrorMessageResponse
import com.bangkit.dantion.data.remote.user.UpdatePasswordBody
import com.bangkit.dantion.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(private val userRepository: UserRepository) : ViewModel() {
    fun updatePassword(token: String, pass: UpdatePasswordBody): LiveData<Result<ErrorMessageResponse>> {
        val updateResponse = MutableLiveData<Result<ErrorMessageResponse>>()
        viewModelScope.launch {
            userRepository.updatePassword(token, pass).collect {
                updateResponse.postValue(it)
            }
        }
        return updateResponse
    }
}