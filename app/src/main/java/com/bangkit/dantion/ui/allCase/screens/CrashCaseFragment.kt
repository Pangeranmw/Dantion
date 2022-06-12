package com.bangkit.dantion.ui.allCase.screens

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.bangkit.dantion.data.Result
import com.bangkit.dantion.data.model.Detection
import com.bangkit.dantion.databinding.FragmentCrashCaseBinding
import com.bangkit.dantion.getCity
import com.bangkit.dantion.setToastLong
import com.bangkit.dantion.ui.allCase.DangerCaseAdapter
import com.bangkit.dantion.ui.viewModel.DataStoreViewModel
import com.bangkit.dantion.ui.viewModel.DetectionViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CrashCaseFragment : Fragment() {
    private lateinit var _binding: FragmentCrashCaseBinding
    private val binding get() = _binding!!
    private val dataStoreViewModel: DataStoreViewModel by viewModels()
    private val detectionViewModel: DetectionViewModel by viewModels()

    private lateinit var crashDangerAdapter: DangerCaseAdapter
    private var crashDetections = ArrayList<Detection>()

    private lateinit var token: String
    private lateinit var city: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCrashCaseBinding.inflate(inflater, container, false)
        val view = binding.root
        getToken()
        return view
    }
    private fun getAllDetections(){
        detectionViewModel.getAllDetections(token).observe(viewLifecycleOwner){res->
            when(res){
                is Result.Loading -> setLoading(true)
                is Result.Success -> {
                    setLoading(false)
                    crashDetections.clear()
                    val crashDetectionsRes = res.data.detections.filter{
                        // get detection only with the same city
                        city.contains(it.city, ignoreCase = true) &&
                        // get detection that only have valid or complete status
                        it.status == "valid" || it.status == "selesai"
                    }.take(5)
                    crashDetections.addAll(crashDetectionsRes)
                    setAdapter(crashDetections)
                }
                is Result.Error -> {
                    setLoading(false)
                    setToastLong(res.error, requireContext())
                }
            }
        }
    }
    private fun setAdapter(detectionList: ArrayList<Detection>){
        crashDangerAdapter = DangerCaseAdapter(detectionList, requireActivity())
        crashDangerAdapter.updateData(detectionList)
        binding.rvCrashCase.layoutManager = LinearLayoutManager(requireContext())
        binding.rvCrashCase.adapter = crashDangerAdapter
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