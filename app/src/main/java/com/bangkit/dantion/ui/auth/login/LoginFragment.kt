package com.bangkit.dantion.ui.auth.login

import android.os.Bundle
import android.util.Log
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
import com.bangkit.dantion.data.Result
import com.bangkit.dantion.data.model.LoginResult
import com.bangkit.dantion.data.remote.user.LoginField
import com.bangkit.dantion.databinding.FragmentLoginBinding
import com.bangkit.dantion.setToastLong
import com.bangkit.dantion.ui.viewModel.DataStoreViewModel
import com.bangkit.dantion.ui.viewModel.AuthViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment : Fragment() {
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    private val authViewModel: AuthViewModel by viewModels()
    private val dataStoreViewModel: DataStoreViewModel by viewModels()
    private lateinit var userLogin: LoginResult

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        val view = binding.root
        dataStoreViewModel.getRegister().observe(viewLifecycleOwner){
            val email = it.email
            if(email!="") binding.etEmail.setText(email)
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
        binding.btnLogin.setOnClickListener {
            val loginBody = LoginField(
                email = binding.etEmail.text.toString(),
                password = binding.etPassword.text.toString()
            )
            authViewModel.loginUser(loginBody).observe(viewLifecycleOwner){ res->
                when(res){
                    is Result.Loading -> setLoading(true)
                    is Result.Success -> {
                        setLoading(false)
                        userLogin = res.data.loginResult
                        dataStoreViewModel.saveLogin()
                        dataStoreViewModel.saveToken(userLogin.token.toString())
                        dataStoreViewModel.saveUser(userLogin)
                        Log.d("tes login",userLogin.toString())
                        if(userLogin.role == "umum") {
                            findNavController().navigate(R.id.action_loginFragment_to_homeActivity)
                        }
                        requireActivity().finish()
                        requireActivity().overridePendingTransition(0, 0)
                    }
                    is Result.Error -> {
                        setLoading(false)
                        setToastLong(res.error, requireContext())
                    }
                }
            }

        }
        return view
    }
    private fun setLoading(state: Boolean){
        binding.progressBar.visibility = if(state) View.VISIBLE else View.INVISIBLE
    }
}