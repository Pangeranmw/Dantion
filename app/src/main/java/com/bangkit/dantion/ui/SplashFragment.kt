package com.bangkit.dantion.ui

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.asLiveData
import androidx.navigation.fragment.findNavController
import com.bangkit.dantion.R
import com.bangkit.dantion.data.UserPreference
import com.bangkit.dantion.ui.onboarding.OnBoardingViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SplashFragment : Fragment() {
    private lateinit var userPreferences: UserPreference
    private val viewModel: OnBoardingViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Handler(Looper.getMainLooper()).postDelayed({
            userPreferences = UserPreference(requireContext())
            viewModel.getOnBoarding().observe(viewLifecycleOwner){ onBoarding->
                userPreferences.login.asLiveData().observe(requireActivity()) { login ->
                    if (onBoarding==null && login == null) {
                        findNavController().navigate(R.id.action_splashFragment_to_viewPagerFragment)
                        requireActivity().overridePendingTransition(0, 0);
                    } else if (onBoarding!=null && login == null) {
                        findNavController().navigate(R.id.action_splashFragment_to_loginFragment)
                        requireActivity().overridePendingTransition(0, 0);
                    } else {
                        findNavController().navigate(R.id.action_splashFragment_to_homeActivity)
                        requireActivity().overridePendingTransition(0, 0);
                    }
                }
            }
        }, 3000)

        return inflater.inflate(R.layout.fragment_splash, container, false)
    }
}