package com.bangkit.dantion.ui.allCase

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.MarginLayoutParams
import android.widget.LinearLayout
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
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

        val tabsViewGroup = tabs.getChildAt(0) as ViewGroup

        for (i in 0 until tabsViewGroup.childCount ) {
            val tab = tabsViewGroup.getChildAt(i)
            val layoutParams = tab.layoutParams as LinearLayout.LayoutParams
            layoutParams.marginEnd = 20
            tab.layoutParams = layoutParams
            tabs.requestLayout()
        }
//        for (i in 0 until tabs.childCount) {
//            val tabChild = (tabs.getChildAt(0) as ViewGroup).getChildAt(i)
//            val layoutParams = tabChild.layoutParams as MarginLayoutParams
//            layoutParams.setMargins(0, 0, 30, 0)
//            tabChild.requestLayout()
//        }
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