package com.bangkit.dantion.ui

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.bangkit.dantion.ui.auth.register.screens.AllCaseFragment
import com.bangkit.dantion.ui.auth.register.screens.CrashCaseFragment
import com.bangkit.dantion.ui.auth.register.screens.CrimeCaseFragment
import com.bangkit.dantion.ui.auth.register.screens.FireCaseFragment

class SectionPagerAdapter(activity: AppCompatActivity) : FragmentStateAdapter(activity) {
    override fun getItemCount(): Int {
        return 4
    }

    override fun createFragment(position: Int): Fragment {
        var fragment: Fragment? = null
        when (position) {
            0 -> fragment = AllCaseFragment()
            1 -> fragment = CrimeCaseFragment()
            2 -> fragment = CrashCaseFragment()
            3 -> fragment = FireCaseFragment()
        }
        return fragment as Fragment
    }

}