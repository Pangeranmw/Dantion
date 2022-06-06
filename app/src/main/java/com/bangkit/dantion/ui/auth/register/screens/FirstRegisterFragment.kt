package com.bangkit.dantion.ui.auth.register.screens

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.bangkit.dantion.R
import com.bangkit.dantion.checkNumber
import com.bangkit.dantion.data.model.User
import com.bangkit.dantion.databinding.FragmentFirstRegisterBinding
import com.bangkit.dantion.emptyData
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FirstRegisterFragment : Fragment() {
    private lateinit var _binding: FragmentFirstRegisterBinding
    private val binding get() = _binding!!
    private val authViewModel: AuthViewModel by viewModels()

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
        binding.etName.doAfterTextChanged { text ->
            val txt = text.toString()
            emptyData(txt, binding.etNameLayout, requireContext())
        }
        binding.etAddress.doAfterTextChanged { text ->
            val txt = text.toString()
            emptyData(txt, binding.etAddressLayout, requireContext())
        }
        binding.etNumber.doAfterTextChanged { text ->
            val txt = text.toString()
            checkNumber(txt, binding.etNumberLayout, requireContext())
        }
        binding.etParentNumber.doAfterTextChanged { text ->
            val txt = text.toString()
            checkNumber(txt, binding.etParentNumberLayout, requireContext())
        }
        return view
    }

    private fun isFilled(): Boolean{
        return binding.etName.text.toString().isNotEmpty() &&
                binding.etAddress.text.toString().isNotEmpty() &&
                binding.etNumber.text.toString().isNotEmpty() &&
                binding.etParentNumber.text.toString().isNotEmpty()
    }
    private fun nextStepAction(viewPager2: ViewPager2?){
        if(isFilled()) {
            savePersonalData()
            viewPager2?.setCurrentItem(1, false)
        }
        else Toast.makeText(requireContext(), getString(R.string.empty_personal_data), Toast.LENGTH_LONG).show()
    }
    private fun savePersonalData(){
        authViewModel.saveUser(
            User(
                name = binding.etName.text.toString(),
                address = binding.etAddress.text.toString(),
                number = binding.etNumber.text.toString(),
                parentNumber = binding.etParentNumber.text.toString()
            )
        )
    }
}