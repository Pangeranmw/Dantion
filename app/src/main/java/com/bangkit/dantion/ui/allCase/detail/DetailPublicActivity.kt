package com.bangkit.dantion.ui.allCase.detail

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.bangkit.dantion.*
import com.bangkit.dantion.data.Result
import com.bangkit.dantion.data.model.Detection
import com.bangkit.dantion.databinding.ActivityDetailPublicBinding
import com.bangkit.dantion.ui.viewModel.DataStoreViewModel
import com.bangkit.dantion.ui.viewModel.DistanceMatrixViewModel
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailPublicActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailPublicBinding
    private lateinit var detection: Detection
    private val dataStoreViewModel: DataStoreViewModel by viewModels()
    private val distanceMatrixViewModel: DistanceMatrixViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailPublicBinding.inflate(layoutInflater)
        setContentView(binding.root)
        detection = intent.getParcelableExtra<Detection>(EXTRA_DATA) as Detection

        val iconUrl = when(detection.type){
            "kejahatan" -> "https://storage.googleapis.com/dangerdetection.appspot.com/ic_crime_location.png"
            "kecelakaan" -> "https://storage.googleapis.com/dangerdetection.appspot.com/ic_crash_location.png"
            else -> "https://storage.googleapis.com/dangerdetection.appspot.com/ic_fire_location.png"
        }
        Log.d("maps id", getString(R.string.MAPS_ID))
        val staticMap = "https://maps.google.com/maps/api/staticmap?markers=icon:$iconUrl|${detection.lat},${detection.lon}&zoom=17&size=400x300&sensor=false&key=${BuildConfig.MAPS_KEY}&map_id=${getString(R.string.MAPS_ID)}"
        getDistanceMatrix(detection.lat, detection.lon)
        Glide.with(this)
            .load(staticMap)
            .into(binding.ivMapsDetail)
        Glide.with(this)
            .load(detection.photo)
            .into(binding.ivProfile)
        binding.tvDetailType.text = detection.type.replaceFirstChar { it.uppercase() }
        binding.tvPelaporName.text = detection.name
        binding.tvLocationAddress.text = getAddress(detection.lat,detection.lon,this)
        binding.tvLocationPlace.text = getCity(detection.lat,detection.lon,this)
        binding.tvTimeDate.text = detection.updatedAt.getDateFromTimeStamp().withDateFormat()
        binding.tvTimeTime.text = detection.updatedAt.getTimeFromTimeStamp().withTimeFormat()

        binding.dangerDetailTitle.setOnClickListener { finish() }
        binding.btnNavigateLocation.setOnClickListener { goNavigate(detection.lat, detection.lon) }
    }

    private fun goNavigate(lat: Double, lon: Double) {
        val gmmIntentUri =
            Uri.parse("google.navigation:q=$lat,$lon")
        val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
        mapIntent.setPackage("com.google.android.apps.maps")
        mapIntent.resolveActivity(packageManager)?.let {
            startActivity(mapIntent)
        }
    }
    private fun getDistanceMatrix(lat: Double, lon: Double){
        dataStoreViewModel.getLatitude().observe(this){ originLat->
            dataStoreViewModel.getLongitude().observe(this){ originLon->
                distanceMatrixViewModel.getDistanceMatrix("$originLat,$originLon","$lat,$lon").observe(this){ res->
                    when(res){
                        is Result.Loading -> setLoading(true)
                        is Result.Success -> {
                            setLoading(false)
                            binding.tvDistanceValue.text = res.data.rows[0].elements[0].distance.text
                            binding.tvDurationValue.text =
                                if(res.data.rows[0].elements[0].duration_in_traffic!=null) res.data.rows[0].elements[0].duration_in_traffic?.text
                                else res.data.rows[0].elements[0].duration.text
                        }
                        is Result.Error -> {
                            setLoading(false)
                            setToastShort(res.error, this)
                        }
                    }
                }
            }
        }
    }
    private fun setLoading(state: Boolean){
        binding.progressBar.visibility = if(state) View.VISIBLE else View.INVISIBLE
    }
    companion object {
        const val EXTRA_DATA = "extra_data"
    }
}