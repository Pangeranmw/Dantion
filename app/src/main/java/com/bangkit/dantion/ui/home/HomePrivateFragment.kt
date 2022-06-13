package com.bangkit.dantion.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.bangkit.dantion.R
import com.bangkit.dantion.databinding.FragmentHomePrivateBinding
import com.bangkit.dantion.ui.viewModel.DataStoreViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomePrivateFragment : Fragment() {
    private lateinit var binding: FragmentHomePrivateBinding
    private val dataStoreViewModel: DataStoreViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomePrivateBinding.inflate(layoutInflater)
        dataStoreViewModel.getUser().observe(viewLifecycleOwner){
            binding.tvName.text = getString(R.string.full_name, it.name)
            binding.tvRole.text = it.role
        }
        startHowToUse()
        return binding.root
    }
    private fun startHowToUse(){
        binding.tvTutorialBtn.setOnClickListener {
            findNavController().navigate(R.id.action_homePrivateFragment_to_howToUseFragment)
        }
    }
}