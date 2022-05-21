package com.bangkit.dantion.ui.onboarding

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
class OnBoardingViewModel @Inject constructor(private val dataStoreRepository: DataStoreRepository): ViewModel() {
    fun saveOnBoarding(){
        viewModelScope.launch(Dispatchers.IO){
            dataStoreRepository.saveOnBoarding()
        }
    }
    fun getOnBoarding(): LiveData<Boolean?>{
        return dataStoreRepository.getOnBoarding().asLiveData()
    }

}