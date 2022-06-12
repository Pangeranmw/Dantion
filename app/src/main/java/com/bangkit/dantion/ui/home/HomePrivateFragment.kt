package com.bangkit.dantion.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bangkit.dantion.databinding.FragmentHomePrivateBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomePrivateFragment : Fragment() {
    private lateinit var binding: FragmentHomePrivateBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomePrivateBinding.inflate(layoutInflater)



        return binding.root
    }
}