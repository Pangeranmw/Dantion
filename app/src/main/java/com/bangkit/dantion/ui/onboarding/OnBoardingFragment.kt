package com.bangkit.dantion.ui.onboarding

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bangkit.dantion.databinding.FragmentOnBoardingBinding
import com.bangkit.dantion.ui.ViewPagerAdapter
import com.bangkit.dantion.ui.onboarding.screens.FirstOnboardingFragment
import com.bangkit.dantion.ui.onboarding.screens.SecondOnboardingFragment
import com.bangkit.dantion.ui.onboarding.screens.ThirdOnboardingFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OnBoardingFragment : Fragment() {
    private var _binding: FragmentOnBoardingBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentOnBoardingBinding.inflate(inflater, container, false)
        val view = binding.root

        val fragmentList = arrayListOf(
            FirstOnboardingFragment(),
            SecondOnboardingFragment(),
            ThirdOnboardingFragment(),
        )
        val adapter = ViewPagerAdapter(
            fragmentList,
            requireActivity().supportFragmentManager,
            lifecycle
        )

        binding.viewPagerOnBoarding.adapter = adapter
//        binding.viewPagerOnBoarding.isUserInputEnabled = false

        return view
    }
}