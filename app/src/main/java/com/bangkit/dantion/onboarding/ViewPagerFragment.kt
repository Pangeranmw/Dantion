package com.bangkit.dantion.onboarding

import android.os.Bundle
import android.transition.TransitionInflater
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bangkit.dantion.R
import com.bangkit.dantion.databinding.FragmentViewPagerBinding
import com.bangkit.dantion.onboarding.screens.FirstOnboardingFragment
import com.bangkit.dantion.onboarding.screens.SecondOnboardingFragment
import com.bangkit.dantion.onboarding.screens.ThirdOnboardingFragment

class ViewPagerFragment : Fragment() {
    private var _binding: FragmentViewPagerBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentViewPagerBinding.inflate(inflater, container, false)
        val view = binding.root

        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_view_pager, container, false)

        val fragmentList = arrayListOf<Fragment>(
            FirstOnboardingFragment(),
            SecondOnboardingFragment(),
            ThirdOnboardingFragment(),
        )
        val adapter = ViewPagerAdapter(
            fragmentList,
            requireActivity().supportFragmentManager,
            lifecycle
        )

        binding.viewPager.adapter = adapter
        binding.viewPager.isUserInputEnabled = false

        return view
    }
}