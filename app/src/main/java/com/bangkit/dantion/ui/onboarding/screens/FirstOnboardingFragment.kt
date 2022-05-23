package com.bangkit.dantion.ui.onboarding.screens

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.bangkit.dantion.R
import com.bangkit.dantion.databinding.FragmentFirstOnboardingBinding
import com.bangkit.dantion.ui.onboarding.OnBoardingViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FirstOnboardingFragment : Fragment() {
    private var _binding: FragmentFirstOnboardingBinding? = null
    private val binding get() = _binding!!
    private val onBoardingViewModel: OnBoardingViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFirstOnboardingBinding.inflate(inflater, container, false)
        val view = binding.root
        val viewPager = activity?.findViewById<ViewPager2>(R.id.view_pager_on_boarding)

        binding.tvSkip.setOnClickListener{
            onBoardingViewModel.saveOnBoarding()
            requireActivity().overridePendingTransition(0, 0)
            findNavController().navigate(R.id.action_viewPagerFragment_to_loginFragment)
        }
        binding.btnNext.setOnClickListener{ viewPager?.setCurrentItem(1,false) }
        binding.ivIndicator2.setOnClickListener{ viewPager?.setCurrentItem(1,false) }
        binding.ivIndicator3.setOnClickListener{ viewPager?.setCurrentItem(2,false) }
        return view
    }
}