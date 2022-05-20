package com.bangkit.dantion.ui.onboarding

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bangkit.dantion.data.UserPreference
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OnBoardingViewModel @Inject constructor(private val userPreference: UserPreference): ViewModel() {
    fun saveOnBoarding(){
        viewModelScope.launch {
            userPreference.saveOnBoarding()
        }
    }
}