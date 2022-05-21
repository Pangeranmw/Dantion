package com.bangkit.dantion.ui.register.screens

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.doAfterTextChanged
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.bangkit.dantion.*
import com.bangkit.dantion.data.model.User
import com.bangkit.dantion.databinding.FragmentFirstRegisterBinding
import com.bangkit.dantion.databinding.FragmentSecondRegisterBinding
import com.bangkit.dantion.ui.register.AuthViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SecondRegisterFragment : Fragment() {
    private lateinit var _binding: FragmentSecondRegisterBinding
    private val binding get() = _binding!!
    private val authViewModel: AuthViewModel by viewModels()
    private lateinit var registerUser: User


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSecondRegisterBinding.inflate(inflater, container, false)
        val view = binding.root
        val viewPager = activity?.findViewById<ViewPager2>(R.id.view_pager_register)

        binding.btnRegister.setOnClickListener {
            if(isFilled()) {
                if(isPasswordSame()){
                    authViewModel.getUser().observe(viewLifecycleOwner){
                        registerUser = User(
                            name = it.name,
                            address = it.address,
                            number = it.number,
                            parentNumber = it.parentNumber,
                            email = binding.etEmail.text.toString(),
                            password = binding.etPassword.text.toString()
                        )
                    }
                    Toast.makeText(requireContext(), getString(R.string.register_success), Toast.LENGTH_LONG).show()
                }
                else Toast.makeText(requireContext(), getString(R.string.password_same), Toast.LENGTH_LONG).show()
            }
            else Toast.makeText(requireContext(), getString(R.string.empty_personal_data), Toast.LENGTH_LONG).show()
        }
        binding.tabPersonalData.setOnClickListener {
            backStepAction(viewPager)
        }
        binding.tvLogin.setOnClickListener{
            findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
        }
        binding.etEmail.doAfterTextChanged { text ->
            val txt = text.toString()
            checkEmail(txt, binding.etEmailLayout, requireContext())
        }
        binding.etPassword.doAfterTextChanged { text ->
            val txt = text.toString()
            checkPassword(txt, binding.etPasswordLayout, requireContext())
        }
        binding.etConfirmPassword.doAfterTextChanged { text ->
            val txt = text.toString()
            checkPassword(txt, binding.etConfirmPasswordLayout, requireContext())
        }
        return view
    }

    private fun isFilled(): Boolean{
        return binding.etConfirmPassword.text.toString().isNotEmpty() &&
                binding.etPassword.text.toString().isNotEmpty() &&
                binding.etEmail.text.toString().isNotEmpty()
    }

    private fun isPasswordSame(): Boolean{
        return binding.etConfirmPassword.text.toString() == binding.etPassword.text.toString()
    }
    fun backStepAction(viewPager2: ViewPager2?){
        viewPager2?.setCurrentItem(0, false)
    }
}