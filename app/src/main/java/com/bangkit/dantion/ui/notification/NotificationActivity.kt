package com.bangkit.dantion.ui.notification

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bangkit.dantion.databinding.ActivityNotificationBinding

class NotificationActivity : AppCompatActivity() {
    private lateinit var binding: ActivityNotificationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNotificationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.dangerDetailTitle.setOnClickListener { finish() }
    }
}