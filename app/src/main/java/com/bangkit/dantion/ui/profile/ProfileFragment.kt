package com.bangkit.dantion.ui.profile

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.bangkit.dantion.BuildConfig
import com.bangkit.dantion.databinding.FragmentProfileBinding
import com.bangkit.dantion.ui.MainActivity
import com.bangkit.dantion.ui.profile.password.PasswordActivity
import com.bangkit.dantion.ui.viewModel.DataStoreViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileFragment : Fragment() {
    private lateinit var binding: FragmentProfileBinding
    private val dataStoreViewModel: DataStoreViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfileBinding.inflate(layoutInflater)
        val view = binding.root

        binding.btnEditProfile.setOnClickListener { startEditActivity() }
        binding.ivProfile.setOnClickListener{ startUpdateUserActivity() }
        binding.tvEditPassword.setOnClickListener { startPasswordActivity() }
        binding.tvAboutApp.setOnClickListener { openWeb() }
        binding.btnLogout.setOnClickListener { logoutUser() }

        return view
    }

    private fun startEditActivity() {
//        val intent = Intent(requireActivity(), ini activity)
//        startActivity(intent)
    }
    private fun startUpdateUserActivity() {
        val intent = Intent(requireActivity(), PasswordActivity::class.java)
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
        val intent = Intent(requireActivity(), MainActivity::class.java)
        startActivity(intent)
        requireActivity().finish()
    }
}