package com.bangkit.dantion.ui.viewModel

import androidx.lifecycle.*
import com.bangkit.dantion.data.remote.ErrorMessageResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.bangkit.dantion.data.Result
import com.bangkit.dantion.data.local.entity.CaseEntity
import com.bangkit.dantion.data.model.Detection
import com.bangkit.dantion.data.remote.detection.GetDetectionDetailResponse
import com.bangkit.dantion.data.remote.detection.GetAllDetectionResponse
import com.bangkit.dantion.data.remote.detection.GetDetectionStatResponse
import com.bangkit.dantion.data.remote.detection.UpdateDetectionBody
import com.bangkit.dantion.data.remote.distanceMatrix.GetMatrixResponse
import com.bangkit.dantion.data.repository.DetectionRepository
import com.bangkit.dantion.data.repository.DistanceMatrixRepository
import okhttp3.MultipartBody
import okhttp3.RequestBody

@HiltViewModel
class DistanceMatrixViewModel @Inject constructor(private val distanceMatrixRepository: DistanceMatrixRepository): ViewModel() {

    fun getDistanceMatrix(origins: String, destination: String): LiveData<Result<GetMatrixResponse>>{
        val distanceMatrixResponse = MutableLiveData<Result<GetMatrixResponse>>()
        viewModelScope.launch {
            distanceMatrixRepository.getDistanceMatrix(origins,destination).collect {
                distanceMatrixResponse.postValue(it)
            }
        }
        return distanceMatrixResponse
    }
}