package com.bangkit.dantion.ui.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bangkit.dantion.data.Result
import com.bangkit.dantion.data.remote.ErrorMessageResponse
import com.bangkit.dantion.data.remote.detection.GetDetectionDetailResponse
import com.bangkit.dantion.data.remote.user.GetDetailUserResponse
import com.bangkit.dantion.data.remote.user.UpdatePasswordBody
import com.bangkit.dantion.data.remote.user.UpdatePhotoUserResponse
import com.bangkit.dantion.data.remote.user.UpdateUserBody
import com.bangkit.dantion.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(private val userRepository: UserRepository) : ViewModel() {
    fun getUserDetail(token: String, id: String): LiveData<Result<GetDetailUserResponse>>{
        val userResponse = MutableLiveData<Result<GetDetailUserResponse>>()
        viewModelScope.launch {
            userRepository.getUserDetail(token, id).collect {
                userResponse.postValue(it)
            }
        }
        return userResponse
    }
    fun updatePassword(token: String, pass: UpdatePasswordBody): LiveData<Result<ErrorMessageResponse>> {
        val updateResponse = MutableLiveData<Result<ErrorMessageResponse>>()
        viewModelScope.launch {
            userRepository.updatePassword(token, pass).collect {
                updateResponse.postValue(it)
            }
        }
        return updateResponse
    }
    fun updateUser(token: String, user: UpdateUserBody): LiveData<Result<ErrorMessageResponse>> {
        val updateResponse = MutableLiveData<Result<ErrorMessageResponse>>()
        viewModelScope.launch {
            userRepository.updateUser(token, user).collect {
                updateResponse.postValue(it)
            }
        }
        return updateResponse
    }
    fun updateUserPhoto(token: String, id: String, photo: MultipartBody.Part): LiveData<Result<UpdatePhotoUserResponse>> {
        val updateResponse = MutableLiveData<Result<UpdatePhotoUserResponse>>()
        viewModelScope.launch {
            userRepository.updateUserPhoto(token, id, photo).collect {
                updateResponse.postValue(it)
            }
        }
        return updateResponse
    }
}