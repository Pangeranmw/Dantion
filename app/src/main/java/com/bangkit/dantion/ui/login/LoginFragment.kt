package com.bangkit.dantion.ui.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.bangkit.dantion.R
import com.bangkit.dantion.checkEmail
import com.bangkit.dantion.checkPassword
import com.bangkit.dantion.databinding.FragmentLoginBinding
import com.bangkit.dantion.ui.register.AuthViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment : Fragment() {
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    private val authViewModel: AuthViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        val view = binding.root
        authViewModel.getUser().observe(viewLifecycleOwner){
            val email = it.email.toString()
            binding.etEmail.setText(email)
        }
        binding.tvRegister.setOnClickListener{
            findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
        }
        binding.etEmail.doAfterTextChanged { text->
            val txt = text.toString()
            checkEmail(txt, binding.etEmailLayout, requireContext())
        }
        binding.etPassword.doAfterTextChanged { text->
            val txt = text.toString()
            checkPassword(txt, binding.etPasswordLayout, requireContext())
        }
        return view
    }
}