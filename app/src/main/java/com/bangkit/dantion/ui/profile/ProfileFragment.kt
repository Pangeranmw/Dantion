package com.bangkit.dantion.ui.profile

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.bangkit.dantion.BuildConfig
import com.bangkit.dantion.R
import com.bangkit.dantion.data.Result
import com.bangkit.dantion.databinding.FragmentProfileBinding
import com.bangkit.dantion.getFirstName
import com.bangkit.dantion.setToastShort
import com.bangkit.dantion.ui.MainActivity
import com.bangkit.dantion.ui.profile.password.PasswordActivity
import com.bangkit.dantion.ui.viewModel.DataStoreViewModel
import com.bangkit.dantion.ui.viewModel.ProfileViewModel
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileFragment : Fragment() {
    private lateinit var binding: FragmentProfileBinding
    private val dataStoreViewModel: DataStoreViewModel by viewModels()
    private val profileViewModel: ProfileViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfileBinding.inflate(layoutInflater)
        val view = binding.root

        initData()

        binding.btnEditProfile.setOnClickListener { startUpdateUserActivity() }
        binding.ivProfile.setOnClickListener{ startUpdateUserActivity() }
        binding.tvEditPassword.setOnClickListener { startPasswordActivity() }
        binding.tvAboutApp.setOnClickListener { openWeb() }
        binding.btnLogout.setOnClickListener { logoutUser() }

        return view
    }

    override fun onResume() {
        super.onResume()
        initData()
    }
    private fun initData(){
        dataStoreViewModel.getToken().observe(viewLifecycleOwner){token->
            dataStoreViewModel.getIdUser().observe(viewLifecycleOwner){ idUser ->
                profileViewModel.getUserDetail(token, idUser).observe(viewLifecycleOwner){
                    when(it){
                        is Result.Loading -> setLoading(true)
                        is Result.Success ->{
                            setLoading(false)
                            val user = it.data.user
                            Glide.with(requireActivity())
                                .load(user.photo)
                                .placeholder(R.drawable.img_profile)
                                .dontAnimate()
                                .into(binding.ivProfile)
                            binding.apply {
                                tvUserNumber.text = user.number.toString()
                                tvUserName.text = user.name.toString()
                                tvUserEmail.text = user.email.toString()
                            }
                        }
                        is Result.Error->{
                            setLoading(false)
                            setToastShort(it.error, requireContext())
                        }
                    }
                }
            }
        }
    }
    private fun startUpdateUserActivity() {
        val intent = Intent(requireActivity(), UpdateUserActivity::class.java)
        startActivity(intent)
    }

    private fun startPasswordActivity() {
        val intent = Intent(requireActivity(), PasswordActivity::class.java)
        startActivity(intent)
    }

    private fun openWeb() {
        val uri = Uri.parse(BuildConfig.WEB_URL)
        val intent = Intent(Intent.ACTION_VIEW, uri)
        startActivity(intent)
    }

    private fun logoutUser() {
        dataStoreViewModel.logout()
//        val navHostFragment = requireActivity().supportFragmentManager.findFragmentById(R.id.fragmentContainerView)
//        navHostFragment.findNavController().navigate(R.id.action_profileFragment2_to_loginFragment)
        findNavController().navigate(R.id.action_profileFragment_to_mainActivity)
        requireActivity().overridePendingTransition(0, 0)
//        val intent = Intent(requireActivity(), MainActivity::class.java)
//        startActivity(intent)
        requireActivity().finish()
    }
    private fun setLoading(state: Boolean){
        binding.progressBar.visibility = if(state) View.VISIBLE else View.INVISIBLE
    }
}