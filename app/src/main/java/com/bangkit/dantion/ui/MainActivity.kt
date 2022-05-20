package com.bangkit.dantion.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.asLiveData
import com.bangkit.dantion.data.UserPreference
import com.bangkit.dantion.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding

    lateinit var userPreferences: UserPreference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setBinding()
//        userPreferences = UserPreference(this)
//        userPreferences.onBoarding.asLiveData().observe(this){
//            val activity = if (it == null) AuthActivity::class else HomeActivity::class
//            startNewActivity(activity)
//        }
    }
    private fun setBinding(){
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}