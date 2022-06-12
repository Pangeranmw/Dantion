package com.bangkit.dantion.ui

import android.Manifest
import android.content.IntentSender
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.bangkit.dantion.R
import com.bangkit.dantion.setToastLong
import com.bangkit.dantion.setToastShort
import com.bangkit.dantion.ui.viewModel.DataStoreViewModel
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import dagger.hilt.android.AndroidEntryPoint
import java.util.concurrent.TimeUnit

@AndroidEntryPoint
class SplashFragment : Fragment() {
    private val dataStoreViewModel: DataStoreViewModel by viewModels()
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationRequest: LocationRequest

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        getMyLastLocation()
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Handler(Looper.getMainLooper()).postDelayed({
            dataStoreViewModel.getOnBoarding().observe(viewLifecycleOwner){ onBoarding->
                dataStoreViewModel.getLogin().observe(viewLifecycleOwner) { login ->
                    if (onBoarding==null && login == null) {
                        findNavController().navigate(R.id.action_splashFragment_to_viewPagerFragment)
                        requireActivity().overridePendingTransition(0, 0)
                    } else if (onBoarding!=null && login == null) {
                        findNavController().navigate(R.id.action_splashFragment_to_loginFragment)
                        requireActivity().overridePendingTransition(0, 0)
                    } else {
                        findNavController().navigate(R.id.action_splashFragment_to_roleUmumActivity)
                        activity?.finish()
                        requireActivity().overridePendingTransition(0, 0)
                    }
                }
            }
        }, 3000)

        return inflater.inflate(R.layout.fragment_splash, container, false)
    }
    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->
            when {
                permissions[Manifest.permission.ACCESS_FINE_LOCATION] ?: false -> {
                    getMyLastLocation()
                }
                permissions[Manifest.permission.ACCESS_COARSE_LOCATION] ?: false -> {
                    getMyLastLocation()
                }
                else -> { }
            }
        }

    private fun checkPermission(permission: String): Boolean {
        return ContextCompat.checkSelfPermission(
            requireContext(),
            permission
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun getMyLastLocation() {
        if (checkPermission(Manifest.permission.ACCESS_FINE_LOCATION) &&
            checkPermission(Manifest.permission.ACCESS_COARSE_LOCATION)
        ) {
            fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
                if (location != null) {
                    dataStoreViewModel.saveLocation(location.latitude, location.longitude)
                } else {
                    setToastLong(getString(R.string.location_not_found),requireContext())
                }
            fusedLocationClient.lastLocation.addOnFailureListener { exception ->
                    if (exception is ResolvableApiException) {
                        try {
                            resolutionLauncher.launch(
                                IntentSenderRequest.Builder(exception.resolution).build()
                            )
                        } catch (sendEx: IntentSender.SendIntentException) {
                            sendEx.message?.let { setToastShort(it,requireContext()) }
                        }
                    }
                }
            }
        } else {
            requestPermissionLauncher.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        }
    }
    private val resolutionLauncher = registerForActivityResult(ActivityResultContracts.StartIntentSenderForResult()) { result ->
        when (result.resultCode) {
            AppCompatActivity.RESULT_OK -> setToastLong("onActivityResult: All location settings are satisfied.",requireContext())
            AppCompatActivity.RESULT_CANCELED -> setToastLong("Anda harus mengaktifkan GPS untuk menggunakan aplikasi ini!",requireContext())
        }
    }
}