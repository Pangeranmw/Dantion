package com.bangkit.dantion.ui

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.asLiveData
import androidx.navigation.fragment.findNavController
import com.bangkit.dantion.R
import com.bangkit.dantion.data.UserPreference
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SplashFragment : Fragment() {
    private lateinit var userPreferences: UserPreference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        Handler(Looper.getMainLooper()).postDelayed({
            userPreferences = UserPreference(requireContext())
            userPreferences.onBoarding.asLiveData().observe(requireActivity()){ onBoarding->
                userPreferences.login.asLiveData().observe(requireActivity()) { login ->
                    if (onBoarding==null &&
//                        !onBoardingFinished() &&
                        login == null) {
                        findNavController().navigate(R.id.action_splashFragment_to_viewPagerFragment)
//                        findNavController().popBackStack()
                        requireActivity().overridePendingTransition(0, 0);
                    } else if (onBoarding!=null &&
                        //onBoardingFinished()
                        login == null) {
                        findNavController().navigate(R.id.action_splashFragment_to_loginFragment)
//                        findNavController().popBackStack()
                        requireActivity().overridePendingTransition(0, 0);
                    } else {
                        findNavController().navigate(R.id.action_splashFragment_to_homeActivity)
//                        findNavController().popBackStack()
                        requireActivity().overridePendingTransition(0, 0);
                    }
                }
            }
        }, 3000)
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_splash, container, false)
    }

    private fun onBoardingFinished(): Boolean{
        val sharedPref = requireActivity().getSharedPreferences("onBoarding", Context.MODE_PRIVATE)
        return sharedPref.getBoolean("Finished", false)
    }
}