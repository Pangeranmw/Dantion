package com.bangkit.dantion.ui.auth.login

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.bangkit.dantion.R
import com.bangkit.dantion.checkEmail
import com.bangkit.dantion.checkPassword
import com.bangkit.dantion.data.Result
import com.bangkit.dantion.data.remote.user.LoginBody
import com.bangkit.dantion.databinding.FragmentLoginBinding
import com.bangkit.dantion.setToastLong
import com.bangkit.dantion.ui.DataStoreViewModel
import com.bangkit.dantion.ui.auth.AuthViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment : Fragment() {
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    private val authViewModel: AuthViewModel by viewModels()
    private val dataStoreViewModel: DataStoreViewModel by viewModels()
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        val view = binding.root
        dataStoreViewModel.getUser().observe(viewLifecycleOwner){
            val email = it.email.toString()
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
            val loginBody = LoginBody(
                email = binding.etEmail.text.toString(),
                password = binding.etPassword.text.toString()
            )
            authViewModel.loginUser(loginBody).observe(viewLifecycleOwner){ res->
                when(res){
                    is Result.Loading -> setLoading(true)
                }
            }
            findNavController().navigate(R.id.action_loginFragment_to_homeActivity)
            activity?.finish()
            requireActivity().overridePendingTransition(0, 0)
        }
        return view
    }

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->
            when {
                permissions[Manifest.permission.ACCESS_FINE_LOCATION] ?: false -> {
                    getMyLastLocation()
                }
                permissions[Manifest.permission.ACCESS_COARSE_LOCATION] ?: false -> {
                    getMyLastLocation()
                }
                else -> { }
            }
        }

    private fun checkPermission(permission: String): Boolean {
        return ContextCompat.checkSelfPermission(
            requireContext(),
            permission
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun getMyLastLocation() {
        if (checkPermission(Manifest.permission.ACCESS_FINE_LOCATION) &&
            checkPermission(Manifest.permission.ACCESS_COARSE_LOCATION)
        ) {
            fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
                if (location != null) {
                    dataStoreViewModel.saveLocation(location.latitude, location.longitude)
                } else {
                    setToastLong(getString(R.string.location_not_found),requireContext())
                }
            }
        } else {
            requestPermissionLauncher.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        }
    }

    private fun setLoading(state: Boolean){
        binding.progressBar.visibility = if(state) View.VISIBLE else View.INVISIBLE
    }
}