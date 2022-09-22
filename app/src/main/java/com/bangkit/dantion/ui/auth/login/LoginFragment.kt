package com.bangkit.dantion.ui.auth.login

import android.Manifest
import android.content.IntentSender
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.bangkit.dantion.*
import com.bangkit.dantion.data.Result
import com.bangkit.dantion.data.model.LoginResult
import com.bangkit.dantion.data.remote.user.LoginField
import com.bangkit.dantion.databinding.FragmentLoginBinding
import com.bangkit.dantion.ui.viewModel.DataStoreViewModel
import com.bangkit.dantion.ui.viewModel.AuthViewModel
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment : Fragment() {
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    private val authViewModel: AuthViewModel by viewModels()
    private val dataStoreViewModel: DataStoreViewModel by viewModels()
    private lateinit var userLogin: LoginResult
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        getMyLastLocation()
    }
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
//                        dataStoreViewModel.saveLogin()
//                        dataStoreViewModel.saveToken(userLogin.token)
//                        dataStoreViewModel.saveIdUser(userLogin.id)
                        dataStoreViewModel.saveUser(userLogin)
                        Log.d("tes login",userLogin.toString())
                        when(userLogin.role){
                            "umum" -> findNavController().navigate(R.id.action_loginFragment_to_homeActivity)
                            else -> findNavController().navigate(R.id.action_loginFragment_to_rolePrivateActivity)
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
                fusedLocationClient.lastLocation.addOnFailureListener { exception ->
                    if (exception is ResolvableApiException) {
                        try {
                            resolutionLauncher.launch(
                                IntentSenderRequest.Builder(exception.resolution).build()
                            )
                        } catch (sendEx: IntentSender.SendIntentException) {
                            sendEx.message?.let { setToastShort(it,requireContext()) }
                        }
                    }
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
    private val resolutionLauncher = registerForActivityResult(ActivityResultContracts.StartIntentSenderForResult()) { result ->
        when (result.resultCode) {
            AppCompatActivity.RESULT_OK -> setToastLong("onActivityResult: All location settings are satisfied.",requireContext())
            AppCompatActivity.RESULT_CANCELED -> setToastLong("Anda harus mengaktifkan GPS untuk menggunakan aplikasi ini!",requireContext())
        }
    }
}