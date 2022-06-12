package com.bangkit.dantion.ui.allCase.detail

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bangkit.dantion.databinding.ActivityDetailPublicBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailPublicActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailPublicBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailPublicBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.dangerDetailTitle.setOnClickListener { finish() }
        binding.btnNavigateLocation.setOnClickListener { goNavigate() }
    }

    private fun goNavigate() {

    }
}