package com.bangkit.dantion.ui.location

import com.bangkit.dantion.R
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import androidx.annotation.RequiresApi
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.bangkit.dantion.*
import com.bangkit.dantion.data.Result
import com.bangkit.dantion.data.model.Detection
import com.bangkit.dantion.databinding.FragmentLocationBinding
import com.bangkit.dantion.ui.home.LatestDangerAdapter
import com.bangkit.dantion.ui.viewModel.DataStoreViewModel
import com.bangkit.dantion.ui.viewModel.DetectionViewModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import dagger.hilt.android.AndroidEntryPoint
import java.time.LocalDateTime


@AndroidEntryPoint
class LocationFragment : Fragment() {
    private var _binding: FragmentLocationBinding? = null
    private val binding get() = _binding!!

    private val detectionViewModel: DetectionViewModel by viewModels()
    private val dataStoreViewModel: DataStoreViewModel by viewModels()

    private var latestDetection = ArrayList<Detection>()

    private lateinit var token: String
    private lateinit var userId: String
    private lateinit var city: String

    private val callback = OnMapReadyCallback { googleMap ->
        with(googleMap){
            uiSettings.isIndoorLevelPickerEnabled = true
            uiSettings.isCompassEnabled = true
            uiSettings.isMapToolbarEnabled = true
            uiSettings.isMyLocationButtonEnabled = true
        }
        googleMap.isMyLocationEnabled = true
        getLocation(googleMap)
        getLatestDetection(googleMap)
        googleMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(requireContext(), R.raw.map_style))
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLocationBinding.inflate(inflater, container, false)
        val view = binding.root
        getToken()
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
    }
    private fun setMapMarker(googleMap: GoogleMap){
        latestDetection.forEach {
            val latLng = LatLng(it.lat, it.lon)
            val locationIcon = when(it.type){
                "kejahatan" -> vectorToBitmap(R.drawable.ic_crime_location, Color.parseColor("#9D3900"))
                "kecelakaan" -> vectorToBitmap(R.drawable.ic_crash_location, Color.parseColor("#FF3D00"))
                else -> vectorToBitmap(R.drawable.ic_fire_location, Color.parseColor("#FF7A00"))
            }
            googleMap.setInfoWindowAdapter(object: GoogleMap.InfoWindowAdapter{
                val myContentsView = View.inflate(context,R.layout.custom_info_window,null)
                override fun getInfoContents(marker: Marker): View? {
                    return null
                }
                override fun getInfoWindow(marker: Marker): View? {
                    val tvTitle: TextView = myContentsView.findViewById(R.id.tv_info_time)
                    tvTitle.text = marker.title
                    val tvSnippet: TextView = myContentsView.findViewById(R.id.tv_info_location)
                    tvSnippet.text = marker.snippet
                    return myContentsView
                }
            })
            googleMap.addMarker(
                MarkerOptions()
                    .position(latLng)
                    .title(getString(R.string.detection_info_time,it.name.getFirstName(),
                        it.updatedAt.getDateFromTimeStamp().withDateFormat(),
                        it.updatedAt.getTimeFromTimeStamp().withTimeFormat()))
                    .snippet(getAddress(it.lat,it.lon,requireContext()))
                    .icon(locationIcon)
            )?.showInfoWindow()
            googleMap.setOnMarkerClickListener { marker ->
                marker.showInfoWindow()
                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(marker.position, 18f))
                return@setOnMarkerClickListener true
            }
        }
        Log.d("DetectionList ", latestDetection.toString())
    }
    private fun vectorToBitmap(@DrawableRes id: Int, @ColorInt color: Int): BitmapDescriptor {
        val vectorDrawable = ResourcesCompat.getDrawable(resources, id, null)
        if (vectorDrawable == null) {
            Log.e("BitmapHelper", "Resource not found")
            return BitmapDescriptorFactory.defaultMarker()
        }
        val bitmap = Bitmap.createBitmap(
            vectorDrawable.intrinsicWidth,
            vectorDrawable.intrinsicHeight,
            Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(bitmap)
        vectorDrawable.setBounds(0, 0, canvas.width, canvas.height)
        DrawableCompat.setTint(vectorDrawable, color)
        vectorDrawable.draw(canvas)
        return BitmapDescriptorFactory.fromBitmap(bitmap)
    }
    private fun getToken(){
        dataStoreViewModel.getToken().observe(viewLifecycleOwner) { token ->
            this.token = token
        }
    }
    private fun getUser(){
        dataStoreViewModel.getUser().observe(viewLifecycleOwner) { user ->
            userId = user.id.toString()
        }
    }
    private fun getLocation(googleMap: GoogleMap){
        dataStoreViewModel.getLatitude().observe(viewLifecycleOwner) { lat ->
            dataStoreViewModel.getLongitude().observe(viewLifecycleOwner) { lon ->
                val currentLocation = LatLng(lat,lon)
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 10f))
                city = getCity(lat,lon,requireContext())
                getUser()
            }
        }
    }
    @RequiresApi(Build.VERSION_CODES.O)
    private fun getLatestDetection(googleMap: GoogleMap){
        detectionViewModel.getAllDetections(token)
        detectionViewModel.allDetectionResponse.observe(viewLifecycleOwner){res->
            when(res){
                is Result.Loading -> setLoading(true)
                is Result.Success -> {
                    setLoading(false)
                    latestDetection.clear()
                    val current = LocalDateTime.now().toString().getDateFromTimeStamp()
                    val nearestDetection = res.data.detections.filter{
                        // get detection only with the same city
                        city.contains(it.city, ignoreCase = true)
                        // get detection that only have valid or complete status
                        && (it.status == "valid" || it.status == "selesai")
                        // get current date detection
//                        && it.updatedAt.getDateFromTimeStamp() == current
                    }
                    latestDetection.addAll(nearestDetection)
                    setMapMarker(googleMap)
                }
                is Result.Error -> {
                    setLoading(false)
                    setToastShort(res.error, requireContext())
                }
            }
        }
    }
    private fun setLoading(state: Boolean){
        binding.progressBar.visibility = if(state) View.VISIBLE else View.INVISIBLE
    }
}