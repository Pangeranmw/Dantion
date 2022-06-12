package com.bangkit.dantion.ui.auth.register

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.Fragment
import com.bangkit.dantion.databinding.FragmentRegisterBinding
import com.bangkit.dantion.ui.ViewPagerAdapter
import com.bangkit.dantion.ui.auth.register.screens.AllCaseFragment
import com.bangkit.dantion.ui.auth.register.screens.CrimeCaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RegisterFragment : Fragment() {
    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requireActivity().window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        val view = binding.root

        val fragmentList = arrayListOf(
            AllCaseFragment(),
            CrimeCaseFragment()
        )
        val adapter = ViewPagerAdapter(
            fragmentList,
            requireActivity().supportFragmentManager,
            lifecycle
        )

        binding.viewPagerRegister.adapter = adapter
        binding.viewPagerRegister.isUserInputEnabled = false

        return view
    }
}