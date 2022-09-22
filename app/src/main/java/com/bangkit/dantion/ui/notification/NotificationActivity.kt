package com.bangkit.dantion.ui.notification

import android.R
import android.R.layout
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import com.bangkit.dantion.*
import com.bangkit.dantion.data.Result
import com.bangkit.dantion.data.local.entity.DetectionReportEntity
import com.bangkit.dantion.data.local.entity.MyDetectionReportEntity
import com.bangkit.dantion.data.mapper.detectionToDetectionReport
import com.bangkit.dantion.data.mapper.detectionToMyDetectionReport
import com.bangkit.dantion.data.model.Detection
import com.bangkit.dantion.databinding.ActivityNotificationBinding
import com.bangkit.dantion.ui.allCase.detail.DetailPublicActivity
import com.bangkit.dantion.ui.viewModel.DataStoreViewModel
import com.bangkit.dantion.ui.viewModel.DetectionViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.*


@AndroidEntryPoint
class NotificationActivity : AppCompatActivity() {
    private lateinit var binding: ActivityNotificationBinding
    private lateinit var myReportAdapter: MyReportNotificationAdapter
    private lateinit var todayCaseReport: ReportNotificationAdapter
    private var city = ""
    private var userId = ""

    private val detectionViewModel: DetectionViewModel by viewModels()
    private val dataStoreViewModel: DataStoreViewModel by viewModels()
    private lateinit var token: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNotificationBinding.inflate(layoutInflater)
        setContentView(binding.root)
        getToken()
        binding.dangerDetailTitle.setOnClickListener { finish() }
    }
    private fun getToken(){
        dataStoreViewModel.getToken().observe(this) { token ->
            this.token = token
            getLocation()
        }
    }
    private fun getLocation(){
        dataStoreViewModel.getLatitude().observe(this) { lat ->
            dataStoreViewModel.getLongitude().observe(this) { lon ->
                city = getCity(lat,lon,this)
                getAllDetection()
            }
        }
    }
    private fun getAllDetection(){
        dataStoreViewModel.getIdUser().observe(this){ id ->
            userId = id.toString()
            detectionViewModel.getMyDetections(token, userId)
            detectionViewModel.myDetectionResponse.observe(this) { myReport ->
                when(myReport){
                    is Result.Loading -> setLoading(true)
                    is Result.Success -> {
                        setLoading(false)
                        detectionViewModel.insertMyDetectionReport(myReport.data.detections, this)
                        detectionViewModel.getAllMyDetectionReport()
                        detectionViewModel.allMyDetectionReport.observe(this){
                            setMyReportToday(it, detectionToMyDetectionReport(myReport.data.detections,this))
                        }
                    }
                    is Result.Error -> {
                        setLoading(false)
                        setToastShort(myReport.error, this)
                    }
                }
            }

            detectionViewModel.getTodayDetections(token, userId, city)
            detectionViewModel.todayDetectionResponse.observe(this) { todayReport ->
                when(todayReport){
                    is Result.Loading -> setLoading(true)
                    is Result.Success -> {
                        setLoading(false)
                        detectionViewModel.insertDetectionReport(todayReport.data.detections, this)
                        detectionViewModel.getAllDetectionReport()
                        detectionViewModel.allDetectionReport.observe(this){
                            setTodayReport(it, detectionToDetectionReport(todayReport.data.detections,this))
                        }
                    }
                    is Result.Error -> {
                        setLoading(false)
                        setToastShort(todayReport.error, this)
                    }
                }
            }
        }
    }

    override fun onRestart() {
        super.onRestart()
        updateData()
    }
    override fun onResume() {
        super.onResume()
        updateData()
    }
    private fun updateData(){
        detectionViewModel.getAllDetectionReport()
        detectionViewModel.getAllMyDetectionReport()
    }
    private fun setLoading(state: Boolean){
        binding.progressBar.visibility = if(state) View.VISIBLE else View.INVISIBLE
    }
    private fun updateDataMyReport(detectionList: List<MyDetectionReportEntity>){
//        myReportAdapter.updateData(detectionList as ArrayList<MyDetectionReportEntity>)
        binding.rvMyReport.layoutManager = LinearLayoutManager(this)
        binding.rvMyReport.adapter = myReportAdapter
//        detectionViewModel.deleteAllMyDetectionReport()
//        detectionViewModel.insertMyDetectionReport(myReportList, this)
    }
    private fun setMyReportToday(list: ArrayList<MyDetectionReportEntity>, detectionList: List<MyDetectionReportEntity>) {
        if(detectionList.isEmpty()) {
            binding.tvNotFoundMyReport.visibility = View.VISIBLE
            val layout = findViewById<TextView>(com.bangkit.dantion.R.id.tv_heading_today_report)
            val params = layout.layoutParams as ConstraintLayout.LayoutParams
            params.setMargins(0, 150, 0, 0)
            layout.layoutParams = params
        }else binding.tvNotFoundMyReport.visibility = View.GONE
        myReportAdapter = MyReportNotificationAdapter(list) { data ->
            detectionViewModel.updateReadMyDetectionReport(data.detectionId,true)
            getDetectionDetail(token, data.detectionId)
        }
        updateDataMyReport(detectionList)
    }
    private fun updateDataReport(detectionList: List<DetectionReportEntity>){
//        todayCaseReport.updateData(detectionList as ArrayList<DetectionReportEntity>)
        binding.rvLatestAcc.layoutManager = LinearLayoutManager(this)
        binding.rvLatestAcc.adapter = todayCaseReport
//        detectionViewModel.deleteAllDetectionReport()
//        detectionViewModel.insertDetectionReport(todayReportList, this)
    }
    private fun setTodayReport(list: ArrayList<DetectionReportEntity>, detectionList: List<DetectionReportEntity>) {
        if(detectionList.isEmpty()) {
            binding.tvNotFoundTodayReport.visibility = View.VISIBLE
        }else binding.tvNotFoundTodayReport.visibility = View.GONE
        todayCaseReport = ReportNotificationAdapter(list){data ->
            detectionViewModel.updateReadDetectionReport(data.detectionId,true)
            getDetectionDetail(token, data.detectionId)
        }
        updateDataReport(detectionList)
    }
    private fun getDetectionDetail(token: String, id: String){
        detectionViewModel.getDetectionDetail(token, id).observe(this){
            when(it){
                is Result.Loading -> setLoading(true)
                is Result.Success -> {
                    setLoading(false)
                    val intent = Intent(this, DetailPublicActivity::class.java)
                    intent.putExtra(DetailPublicActivity.EXTRA_DATA, it.data.detection)
                    startActivity(intent)
                }
                is Result.Error -> {
                    setLoading(false)
                    setToastShort(it.error,this)
                }
            }
        }
    }
}