package com.bangkit.dantion.ui.register.screens

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.bangkit.dantion.R
import com.bangkit.dantion.data.model.User
import com.bangkit.dantion.databinding.FragmentFirstOnboardingBinding
import com.bangkit.dantion.databinding.FragmentFirstRegisterBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FirstRegisterFragment : Fragment() {
    private lateinit var _binding: FragmentFirstRegisterBinding
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFirstRegisterBinding.inflate(inflater, container, false)
        val view = binding.root
        val viewPager = activity?.findViewById<ViewPager2>(R.id.view_pager_register)

        binding.btnNext.setOnClickListener {
            nextStepAction(viewPager)
        }
        binding.tabAccountInfo.setOnClickListener {
            nextStepAction(viewPager)
        }
        binding.tvLogin.setOnClickListener{
            findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
        }
        return view
    }

    private fun isFilled(): Boolean{
        return binding.etName.text.isNotEmpty() &&
                binding.etAddress.text.isNotEmpty() &&
                binding.etNumber.text.isNotEmpty() &&
                binding.etParentNumber.text.isNotEmpty()
    }
    private fun nextStepAction(viewPager2: ViewPager2?){
        if(isFilled()) {
            savePersonalData()
            viewPager2?.setCurrentItem(1, false)
        }
        else Toast.makeText(requireContext(), getString(R.string.empty_personal_data), Toast.LENGTH_LONG).show()
    }
    private fun savePersonalData(){
        val sharedPref = requireActivity().getSharedPreferences("register", Context.MODE_PRIVATE)
        val editor = sharedPref.edit()
        editor.putString("name", binding.etName.text.toString())
        editor.putString("address", binding.etAddress.text.toString())
        editor.putString("number", binding.etNumber.text.toString())
        editor.putString("parentNumber", binding.etParentNumber.text.toString())
        editor.apply()
    }
}