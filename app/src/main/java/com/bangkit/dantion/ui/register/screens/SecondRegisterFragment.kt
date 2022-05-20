package com.bangkit.dantion.ui.register.screens

import android.content.Context
import android.content.SharedPreferences
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
import com.bangkit.dantion.databinding.FragmentFirstRegisterBinding
import com.bangkit.dantion.databinding.FragmentSecondRegisterBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SecondRegisterFragment : Fragment() {
    private lateinit var _binding: FragmentSecondRegisterBinding
    private val binding get() = _binding!!
    private lateinit var sharedPref: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSecondRegisterBinding.inflate(inflater, container, false)
        val view = binding.root
        val viewPager = activity?.findViewById<ViewPager2>(R.id.view_pager_register)
        sharedPref = requireActivity().getSharedPreferences("register", Context.MODE_PRIVATE)

        binding.etEmail.setText(getAccountData().name+getAccountData().address+getAccountData().number+getAccountData().parentNumber)
        binding.btnRegister.setOnClickListener {
            if(isFilled()) {
                if(isPasswordSame()){
                    val user = User(
                        name = getAccountData().name,
                        address = getAccountData().address,
                        number = getAccountData().number,
                        parentNumber = getAccountData().parentNumber,
                        email = binding.etEmail.text.toString(),
                        password = binding.etPassword.text.toString()
                    )
                    Toast.makeText(requireContext(), getString(R.string.register_success), Toast.LENGTH_LONG).show()
                    sharedPref.edit().clear().apply()
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
        return view
    }

    private fun isFilled(): Boolean{
        return binding.etConfirmPassword.text.isNotEmpty() &&
                binding.etPassword.text.isNotEmpty() &&
                binding.etEmail.text.isNotEmpty()
    }

    private fun isPasswordSame(): Boolean{
        return binding.etConfirmPassword.text.toString() == binding.etPassword.text.toString()
    }
    fun backStepAction(viewPager2: ViewPager2?){
        viewPager2?.setCurrentItem(0, false)
    }
    private fun getEmail(): String? {
        return sharedPref.getString("email", "")
    }
    private fun getAccountData(): User {
        return User(
            name = sharedPref.getString("name", ""),
            address = sharedPref.getString("address", ""),
            number = sharedPref.getString("number", ""),
            parentNumber = sharedPref.getString("parentNumber", ""),
        )
    }
}