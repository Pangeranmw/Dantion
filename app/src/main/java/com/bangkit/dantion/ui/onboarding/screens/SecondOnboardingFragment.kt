package com.bangkit.dantion.ui.onboarding.screens

import android.content.Context
import android.media.MediaPlayer
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.bangkit.dantion.R
import com.bangkit.dantion.databinding.FragmentSecondOnboardingBinding
import com.bangkit.dantion.ui.onboarding.OnBoardingViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SecondOnboardingFragment : Fragment() {
    private var _binding: FragmentSecondOnboardingBinding? = null
    private val binding get() = _binding!!
    private val onBoardingViewModel: OnBoardingViewModel by viewModels()
    private var mediaState: Boolean = false

    private lateinit var mediaPlayer: MediaPlayer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mediaPlayer = MediaPlayer.create(context, R.raw.horn_pitchup)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSecondOnboardingBinding.inflate(inflater, container, false)
        val view = binding.root
        val viewPager = activity?.findViewById<ViewPager2>(R.id.view_pager_on_boarding)

        binding.btnHorn.setOnClickListener{
            if(!mediaState) {
                mediaState=true
                binding.btnHorn.background = ContextCompat.getDrawable(requireContext(), R.drawable.ic_pause_onboarding)
                mediaPlayer.start()
            }
            else {
                binding.btnHorn.background = ContextCompat.getDrawable(requireContext(), R.drawable.ic_play_onboarding)
                mediaState=false
                mediaPlayer.stop()
                mediaPlayer = MediaPlayer.create(context, R.raw.horn_pitchup)
            }
        }
        binding.tvSkip.setOnClickListener {
            onBoardingViewModel.saveOnBoarding()
            requireActivity().overridePendingTransition(0, 0)
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
    override fun onResume() {
        super.onResume()
        mediaPlayer = MediaPlayer.create(context, R.raw.horn_pitchup)
        binding.btnHorn.background = ContextCompat.getDrawable(requireContext(), R.drawable.ic_play_onboarding)
    }
    override fun onPause() {
        super.onPause()
        mediaPlayer.stop()
    }
}