package com.bangkit.dantion.ui.allCase.screens

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.bangkit.dantion.*
import com.bangkit.dantion.data.Result
import com.bangkit.dantion.data.local.entity.CaseEntity
import com.bangkit.dantion.data.mapper.detectionToCaseEntity
import com.bangkit.dantion.data.model.Detection
import com.bangkit.dantion.data.remote.user.RegisterField
import com.bangkit.dantion.databinding.FragmentCrashCaseBinding
import com.bangkit.dantion.databinding.FragmentCrimeCaseBinding
import com.bangkit.dantion.databinding.FragmentSecondRegisterBinding
import com.bangkit.dantion.ui.allCase.DangerCaseAdapter
import com.bangkit.dantion.ui.home.LatestDangerAdapter
import com.bangkit.dantion.ui.viewModel.DataStoreViewModel
import com.bangkit.dantion.ui.viewModel.AuthViewModel
import com.bangkit.dantion.ui.viewModel.DetectionViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CrimeCaseFragment : Fragment() {
    private lateinit var _binding: FragmentCrimeCaseBinding
    private val binding get() = _binding!!
    private val dataStoreViewModel: DataStoreViewModel by viewModels()
    private val detectionViewModel: DetectionViewModel by viewModels()

    private lateinit var crimeDangerAdapter: DangerCaseAdapter
    private var crimeDetections = ArrayList<Detection>()

    private lateinit var token: String
    private lateinit var city: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCrimeCaseBinding.inflate(inflater, container, false)
        val view = binding.root
        getToken()
        return view
    }
    private fun getAllDetections(){
        detectionViewModel.getAllDetections(token)
        detectionViewModel.allDetectionResponse.observe(viewLifecycleOwner){res->
            when(res){
                is Result.Loading -> setLoading(true)
                is Result.Success -> {
                    setLoading(false)
                    crimeDetections.clear()
                    val crimeDetectionRes = res.data.detections.filter{
                        // get detection only with the same city
                        city.contains(it.city, ignoreCase = true) &&
                        // get detection that only have valid or complete status
                        (it.status == "valid" || it.status == "selesai") &&
                        // get only crime type of danger
                        it.type == "kejahatan"
                    }
                    crimeDetections.addAll(crimeDetectionRes)
                    detectionViewModel.getAllDangerCase().observe(viewLifecycleOwner){
                        setAdapter(it, detectionToCaseEntity(crimeDetections))
                    }
                }
                is Result.Error -> {
                    setLoading(false)
                    setToastLong(res.error, requireContext())
                }
            }
        }
    }
    private fun setAdapter(currentDetection: ArrayList<CaseEntity>, detectionList: List<CaseEntity>){
        if(detectionList.isEmpty()) binding.tvNotFound.visibility = View.VISIBLE
        crimeDangerAdapter = DangerCaseAdapter(currentDetection, requireActivity())
        updateData(detectionList)
        binding.rvCrimeCase.layoutManager = LinearLayoutManager(requireContext())
        binding.rvCrimeCase.adapter = crimeDangerAdapter
    }
    private fun updateData(detectionList: List<CaseEntity>){
        crimeDangerAdapter.updateData(detectionList as ArrayList<CaseEntity>)
        detectionViewModel.deleteAllDangerCase()
        detectionViewModel.insertDangerCase(crimeDetections)
    }
    private fun getToken(){
        dataStoreViewModel.getToken().observe(viewLifecycleOwner){
            token = it
            getLocation()
        }
    }
    private fun getLocation(){
        dataStoreViewModel.getLatitude().observe(viewLifecycleOwner){ lat->
            dataStoreViewModel.getLongitude().observe(viewLifecycleOwner){ lon->
                city = getCity(lat,lon,requireContext())
                getAllDetections()
            }
        }
    }
    private fun setLoading(state: Boolean){
        binding.progressBar.visibility = if(state) View.VISIBLE else View.INVISIBLE
    }
}