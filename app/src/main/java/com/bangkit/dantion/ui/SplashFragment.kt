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
import androidx.appcompat.app.AppCompatDelegate
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
                        dataStoreViewModel.getUser().observe(viewLifecycleOwner){
                            when(it.role){
                                "umum" -> findNavController().navigate(R.id.action_splashFragment_to_roleUmumActivity)
                                else -> findNavController().navigate(R.id.action_splashFragment_to_rolePrivateActivity)
                            }
                        }
                        activity?.finish()
                        requireActivity().overridePendingTransition(0, 0)
                    }
                }
            }
        }, 3000)

        return inflater.inflate(R.layout.fragment_splash, container, false)
    }
}