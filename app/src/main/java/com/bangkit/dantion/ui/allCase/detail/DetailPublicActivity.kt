package com.bangkit.dantion.ui.allCase.detail

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bangkit.dantion.*
import com.bangkit.dantion.data.local.entity.CaseEntity
import com.bangkit.dantion.data.model.Detection
import com.bangkit.dantion.databinding.ActivityDetailPublicBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailPublicActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailPublicBinding
    private lateinit var detection: Detection

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailPublicBinding.inflate(layoutInflater)
        setContentView(binding.root)
        detection = intent.getParcelableExtra<Detection>(EXTRA_DATA) as Detection

        binding.tvDetailType.text = detection.type
        binding.tvPelaporName.text = detection.name
        binding.tvLocationAddress.text = getAddress(detection.lat,detection.lon,this)
        binding.tvLocationPlace.text = getCity(detection.lat,detection.lon,this)
        binding.tvTimeDate.text = detection.updatedAt.getDateFromTimeStamp().withDateFormat()
        binding.tvTimeTime.text = detection.updatedAt.getTimeFromTimeStamp().withTimeFormat()

        binding.dangerDetailTitle.setOnClickListener { finish() }
        binding.btnNavigateLocation.setOnClickListener { goNavigate() }
    }

    private fun goNavigate() {

    }
    companion object {
        const val EXTRA_DATA = "extra_data"
    }
}