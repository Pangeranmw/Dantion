package com.bangkit.dantion.ui.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.bangkit.dantion.R
import com.bangkit.dantion.databinding.FragmentHowToUseBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HowToUseFragment : Fragment() {
    private var _binding: FragmentHowToUseBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHowToUseBinding.inflate(inflater, container, false)
        val view = binding.root
        backToHome()
        return view
    }
    private fun backToHome(){
        binding.btnBackHowToUse.setOnClickListener {
            findNavController().navigate(R.id.action_howToUseFragment_to_homeFragment)
        }
    }
}