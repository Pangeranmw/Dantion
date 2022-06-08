package com.bangkit.dantion.ui.home

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.HapticFeedbackConstants
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.bangkit.dantion.*
import com.bangkit.dantion.data.Result
import com.bangkit.dantion.data.model.Detection
import com.bangkit.dantion.data.model.DetectionStat
import com.bangkit.dantion.databinding.ActivityHomeBinding
import com.bangkit.dantion.ui.viewModel.DataStoreViewModel
import com.bangkit.dantion.ui.viewModel.DetectionViewModel
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class HomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding
    private lateinit var latestDangerAdapter: LatestDangerAdapter

    private val detectionViewModel: DetectionViewModel by viewModels()
    private val dataStoreViewModel: DataStoreViewModel by viewModels()

    private var latestDetection = ArrayList<Detection>()
    private lateinit var stat: DetectionStat

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        supportActionBar?.hide()
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        dataStoreViewModel.getUser().observe(this){ user->
            val firstName = user.name.toString().getFirstName()
            Glide.with(this)
                .load(user.photo)
                .into(binding.ivProfile)
            binding.tvName.text = getString(R.string.full_name, firstName)
            dataStoreViewModel.getLatitude().observe(this){ lat->
                dataStoreViewModel.getLongitude().observe(this){ lon ->
                    dataStoreViewModel.getToken().observe(this){ token ->
                        detectionViewModel.getAllDetections(token).observe(this){res->
                            when(res){
                                is Result.Loading -> setLoading(true)
                                is Result.Success -> {
                                    setLoading(false)
                                    latestDetection.clear()
                                    val nearestDetection = res.data.detections.filter{(getCity(lat,lon,this).contains(it.city, ignoreCase = true))}.take(5)
                                    latestDetection.addAll(nearestDetection)
                                    setLatestCase(latestDetection)
                                }
                                is Result.Error -> {
                                    setLoading(false)
                                    setToastShort(res.error, this)
                                }
                            }
                        }
                        detectionViewModel.getDetectionStat().observe(this){res->
                            when(res){
                                is Result.Loading -> setLoading(true)
                                is Result.Success -> {
                                    stat = res.data.stat
                                    binding.tvStatCrimeNumber.text = stat.kejahatan.toString()
                                    binding.tvStatAccNumber.text = stat.kecelakaan.toString()
                                    binding.tvStatFireNumber.text = stat.kebakaran.toString()
                                }
                                is Result.Error -> setLoading(false)
                            }
                        }
                    }
                }
            }
        }
        binding.btnEmergency.performHapticFeedback(HapticFeedbackConstants.LONG_PRESS)
        binding.tvHoldTime.text = getString(R.string.button_hold_time, 1)
        binding.btnEmergency.setOnLongClickListener{
//            it.setOnVeryLongClickListener{
//                binding.layoutEmergencyStart.visibility = View.VISIBLE
//                binding.btnEmergency.visibility = View.INVISIBLE
//            }
            binding.layoutEmergencyStart.visibility = View.VISIBLE
            binding.btnEmergency.visibility = View.INVISIBLE

            return@setOnLongClickListener true
        }
        binding.btnCancelRecord.setOnClickListener {
            binding.layoutEmergencyStart.visibility = View.INVISIBLE
            binding.btnEmergency.visibility = View.VISIBLE
        }
    }
    private fun setLatestCase(list: ArrayList<Detection>) {
        latestDangerAdapter = LatestDangerAdapter(list, this)
        binding.rvLatestCase.layoutManager = LinearLayoutManager(this)
        binding.rvLatestCase.adapter = latestDangerAdapter
    }
    private fun setLoading(state: Boolean){
        binding.progressBar.visibility = if(state) View.VISIBLE else View.INVISIBLE
    }
}