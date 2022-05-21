package com.bangkit.dantion.ui.onboarding.screens

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.bangkit.dantion.R
import com.bangkit.dantion.databinding.FragmentSecondOnboardingBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job

@AndroidEntryPoint
class SecondOnboardingFragment : Fragment() {
    private var _binding: FragmentSecondOnboardingBinding? = null
    private val binding get() = _binding!!

//    private val viewModel: OnBoardingViewModel by viewModels()
    private lateinit var job: Job
//    override val coroutineContext: CoroutineContext
//        get() = Dispatchers.Main + job

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
        _binding = null
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentSecondOnboardingBinding.inflate(inflater, container, false)
        val view = binding.root
        val viewPager = activity?.findViewById<ViewPager2>(R.id.view_pager_on_boarding)
        job = Job()


        binding.tvSkip.setOnClickListener {
//            viewModel.saveOnBoarding()
//            coroutineScope {
//                launch {
//                    onBoardingFinished()
//                    findNavController().navigate(R.id.action_viewPagerFragment_to_loginFragment)
//                }
//            }
            requireActivity().overridePendingTransition(0, 0)
            onBoardingFinished()
            findNavController().navigate(R.id.action_viewPagerFragment_to_loginFragment)
        }
        binding.btnNext.setOnClickListener{
            viewPager?.setCurrentItem(2,false)
        }
        binding.ivIndicator1.setOnClickListener{
            viewPager?.setCurrentItem(0,false)
        }
        binding.ivIndicator3.setOnClickListener{
            viewPager?.setCurrentItem(2,false)
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