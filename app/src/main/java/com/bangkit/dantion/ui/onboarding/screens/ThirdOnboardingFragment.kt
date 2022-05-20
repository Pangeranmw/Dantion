package com.bangkit.dantion.ui.onboarding.screens

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.bangkit.dantion.R
import com.bangkit.dantion.databinding.FragmentThirdOnboardingBinding
import com.bangkit.dantion.ui.onboarding.OnBoardingViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@AndroidEntryPoint
class ThirdOnboardingFragment : Fragment() {
    private var _binding: FragmentThirdOnboardingBinding? = null
    private val binding get() = _binding!!
//    private val viewModel: OnBoardingViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentThirdOnboardingBinding.inflate(inflater, container, false)
        val view = binding.root
        val viewPager = activity?.findViewById<ViewPager2>(R.id.view_pager_on_boarding)
        binding.btnStart.setOnClickListener{
            onBoardingFinished()
//            lifecycleScope.launch {
//                viewModel.saveOnBoarding()
//                withContext(Dispatchers.Main) {
//                    findNavController().navigate(R.id.action_viewPagerFragment_to_loginFragment)
//                }
//            }
            requireActivity().overridePendingTransition(0, 0)
            findNavController().navigate(R.id.action_viewPagerFragment_to_loginFragment)
        }
        binding.ivIndicator2.setOnClickListener{
            viewPager?.setCurrentItem(1,false)
        }
        binding.ivIndicator1.setOnClickListener{
            viewPager?.setCurrentItem(0,false)
        }
        return view
    }
    private fun onBoardingFinished(){
        val sharedPref = requireActivity().getSharedPreferences("onBoarding", Context.MODE_PRIVATE)
        val editor = sharedPref.edit()
        editor.putBoolean("Finished", true)
        editor.apply()
    }
}