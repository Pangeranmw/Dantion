package com.bangkit.dantion.ui.onboarding.screens

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.bangkit.dantion.R
import com.bangkit.dantion.databinding.FragmentFourthOnboardingBinding
import com.bangkit.dantion.ui.DataStoreViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FourthOnboardingFragment : Fragment() {
    private var _binding: FragmentFourthOnboardingBinding? = null
    private val binding get() = _binding!!
    private val dataStoreViewModel: DataStoreViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFourthOnboardingBinding.inflate(inflater, container, false)
        val view = binding.root
        val viewPager = activity?.findViewById<ViewPager2>(R.id.view_pager_on_boarding)
        binding.btnStart.setOnClickListener{
            dataStoreViewModel.saveOnBoarding()
            requireActivity().overridePendingTransition(0, 0)
            findNavController().navigate(R.id.action_viewPagerFragment_to_loginFragment)
        }
        binding.ivIndicator2.setOnClickListener{
            viewPager?.setCurrentItem(1,false)
        }
        binding.ivIndicator1.setOnClickListener{
            viewPager?.setCurrentItem(0,false)
        }
        binding.ivIndicator3.setOnClickListener{
            viewPager?.setCurrentItem(2,false)
        }
        return view
    }
}