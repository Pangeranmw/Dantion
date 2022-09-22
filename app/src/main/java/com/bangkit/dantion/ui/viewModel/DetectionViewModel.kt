package com.bangkit.dantion.ui.viewModel

import android.content.Context
import androidx.lifecycle.*
import com.bangkit.dantion.data.remote.ErrorMessageResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.bangkit.dantion.data.Result
import com.bangkit.dantion.data.local.entity.CaseEntity
import com.bangkit.dantion.data.local.entity.DetectionReportEntity
import com.bangkit.dantion.data.local.entity.MyDetectionReportEntity
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
    private val _allDetectionResponse = MutableLiveData<Result<GetAllDetectionResponse>>()
    val allDetectionResponse: LiveData<Result<GetAllDetectionResponse>>
        get() = _allDetectionResponse
    fun getAllDetections(token: String){
        viewModelScope.launch {
            detectionRepository.getAllDetections(token).collect {
                _allDetectionResponse.value = it
            }
        }
    }
    private val _myDetectionResponse = MutableLiveData<Result<GetAllDetectionResponse>>()
    val myDetectionResponse: LiveData<Result<GetAllDetectionResponse>>
        get() = _myDetectionResponse
    fun getMyDetections(token: String, id: String){
        viewModelScope.launch {
            detectionRepository.getMyDetections(token,id).collect {
                _myDetectionResponse.value = it
            }
        }
    }
    private val _todayDetectionResponse = MutableLiveData<Result<GetAllDetectionResponse>>()
    val todayDetectionResponse: LiveData<Result<GetAllDetectionResponse>>
        get() = _todayDetectionResponse
    fun getTodayDetections(token: String, id: String, city: String){
        viewModelScope.launch {
            detectionRepository.getTodayDetections(token,id,city).collect {
                _todayDetectionResponse.value = it
            }
        }
    }
    fun getDetectionDetail(token: String, id: String): LiveData<Result<GetDetectionDetailResponse>>{
        val detectionDetailResponse = MutableLiveData<Result<GetDetectionDetailResponse>>()
        viewModelScope.launch {
            detectionRepository.getDetectionDetail(token, id).collect {
                detectionDetailResponse.postValue(it)
            }
        }
        return detectionDetailResponse
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

    fun insertDetectionReport(detection: List<Detection>, context: Context){
        viewModelScope.launch {
            detectionRepository.insertDetectionReport(detection, context)
        }
    }
    fun deleteAllDetectionReport(){
        viewModelScope.launch {
            detectionRepository.deleteAllDetectionReport()
        }
    }
    fun updateReadDetectionReport(id: String, isRead: Boolean){
        viewModelScope.launch {
            detectionRepository.updateReadDetectionReport(id, isRead)
        }
    }
    fun readAllReport(){
        viewModelScope.launch {
            detectionRepository.readAllReport()
        }
    }
    private val _allDetectionReport = MutableLiveData<ArrayList<DetectionReportEntity>>()
    val allDetectionReport: LiveData<ArrayList<DetectionReportEntity>>
        get() = _allDetectionReport
    fun getAllDetectionReport(){
        viewModelScope.launch {
            detectionRepository.getAllDetectionReport().collect {
                _allDetectionReport.value = it
            }
        }
    }
//    fun getAllDetectionReport(): LiveData<ArrayList<DetectionReportEntity>>{
//        viewModelScope.launch {
//            detectionRepository.getAllDetectionReport().collect {
//                allDangerCase.postValue(it)
//            }
//        }
//        return allDangerCase
//    }

    fun insertMyDetectionReport(detection: List<Detection>, context: Context){
        viewModelScope.launch {
            detectionRepository.insertMyDetectionReport(detection, context)
        }
    }
    fun deleteAllMyDetectionReport(){
        viewModelScope.launch {
            detectionRepository.deleteAllMyDetectionReport()
        }
    }
    fun updateReadMyDetectionReport(id: String, isRead: Boolean){
        viewModelScope.launch {
            detectionRepository.updateReadMyDetectionReport(id, isRead)
        }
    }
    fun readAllMyReport(){
        viewModelScope.launch {
            detectionRepository.readAllMyReport()
        }
    }

    private val _allMyDetectionReport = MutableLiveData<ArrayList<MyDetectionReportEntity>>()
    val allMyDetectionReport: LiveData<ArrayList<MyDetectionReportEntity>>
        get() = _allMyDetectionReport
    fun getAllMyDetectionReport(){
        viewModelScope.launch {
            detectionRepository.getAllMyDetectionReport().collect {
                _allMyDetectionReport.value = it
            }
        }
    }

    fun getUnreadNotification(): LiveData<Int>{
        val unreadNotif = MutableLiveData<Int>()
        viewModelScope.launch {
            detectionRepository.getUnreadNotification().collect {
                unreadNotif.postValue(it)
            }
        }
        return unreadNotif
    }
}