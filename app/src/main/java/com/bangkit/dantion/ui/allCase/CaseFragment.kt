package com.bangkit.dantion.ui.allCase

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import com.bangkit.dantion.R
import com.bangkit.dantion.databinding.FragmentCaseBinding
import com.bangkit.dantion.ui.SectionPagerAdapter
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CaseFragment : Fragment() {
    private var _binding: FragmentCaseBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCaseBinding.inflate(inflater, container, false)
        val view = binding.root

        val sectionsPagerAdapter = SectionPagerAdapter(requireActivity() as AppCompatActivity)
        val viewPager = binding.caseViewPager
        viewPager.adapter = sectionsPagerAdapter
        val tabs = binding.caseTabs
        TabLayoutMediator(tabs, viewPager) { tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()

        return view
    }
    companion object {
        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.all,
            R.string.crime,
            R.string.accident,
            R.string.fire,
        )
    }
}