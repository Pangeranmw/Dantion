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
import com.bangkit.dantion.data.repository.DetectionRepository
import okhttp3.MultipartBody
import okhttp3.RequestBody

@HiltViewModel
class DetectionViewModel @Inject constructor(private val detectionRepository: DetectionRepository): ViewModel() {

    fun addNewDetection(
        token: String,
        recordUrl: MultipartBody.Part,
        city: RequestBody,
        lat: RequestBody,
        lon: RequestBody,
        type: RequestBody,
        userId: RequestBody
    ): LiveData<Result<ErrorMessageResponse>>{
        val addDetectionResponse = MutableLiveData<Result<ErrorMessageResponse>>()
        viewModelScope.launch {
            detectionRepository.addNewDetection(token, recordUrl, city, lat, lon, type, userId).collect {
                addDetectionResponse.postValue(it)
            }
        }
        return addDetectionResponse
    }
    fun updateDetections(token: String, updateField: UpdateDetectionBody): LiveData<Result<ErrorMessageResponse>>{
        val updateDetectionResponse = MutableLiveData<Result<ErrorMessageResponse>>()
        viewModelScope.launch {
            detectionRepository.updateDetections(token, updateField).collect {
                updateDetectionResponse.postValue(it)
            }
        }
        return updateDetectionResponse
    }
    fun getDetectionStat(): LiveData<Result<GetDetectionStatResponse>>{
        val detectionStatResponse = MutableLiveData<Result<GetDetectionStatResponse>>()
        viewModelScope.launch {
            detectionRepository.getDetectionStat().collect {
                detectionStatResponse.postValue(it)
            }
        }
        return detectionStatResponse
    }
    fun getAllDetections(token: String): LiveData<Result<GetAllDetectionResponse>>{
        val allDetectionResponse = MutableLiveData<Result<GetAllDetectionResponse>>()
        viewModelScope.launch {
            detectionRepository.getAllDetections(token).collect {
                allDetectionResponse.postValue(it)
            }
        }
        return allDetectionResponse
    }
    fun getDetectionDetail(token: String, id: String): LiveData<Result<GetDetectionDetailResponse>>{
        val allDetectionResponse = MutableLiveData<Result<GetDetectionDetailResponse>>()
        viewModelScope.launch {
            detectionRepository.getDetectionDetail(token, id).collect {
                allDetectionResponse.postValue(it)
            }
        }
        return allDetectionResponse
    }
    fun deleteDetection(token: String, id: String): LiveData<Result<ErrorMessageResponse>>{
        val allDetectionResponse = MutableLiveData<Result<ErrorMessageResponse>>()
        viewModelScope.launch {
            detectionRepository.deleteDetection(token, id).collect {
                allDetectionResponse.postValue(it)
            }
        }
        return allDetectionResponse
    }
    fun insertDangerCase(detection: List<Detection>){
        viewModelScope.launch {
            detectionRepository.insertDangerCase(detection)
        }
    }
    fun deleteAllDangerCase(){
        viewModelScope.launch {
            detectionRepository.deleteAllDangerCase()
        }
    }
    fun getAllDangerCase(): LiveData<ArrayList<CaseEntity>>{
        val allDangerCase = MutableLiveData<ArrayList<CaseEntity>>()
        viewModelScope.launch {
            detectionRepository.getAllDangerCase().collect {
                allDangerCase.postValue(it)
            }
        }
        return allDangerCase
    }
}