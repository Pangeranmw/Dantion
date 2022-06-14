package com.bangkit.dantion.ui

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.bangkit.dantion.R
import com.bangkit.dantion.databinding.ActivityRolePrivateBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RolePrivateActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRolePrivateBinding

    private val permissions = arrayOf(Manifest.permission.RECORD_AUDIO)
    private val REQUEST_RECORD_AUDIO = 1337
    private var permissionGranted = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        supportActionBar?.hide()
        binding = ActivityRolePrivateBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // ask record permission
        permissionGranted = ActivityCompat.checkSelfPermission(this, permissions[0]) == PackageManager.PERMISSION_GRANTED
        if (!permissionGranted) ActivityCompat.requestPermissions(this, permissions, REQUEST_RECORD_AUDIO)
        val navController = findNavController(R.id.fragment_bottom_nav_private)
        binding.bottomNavPrivate.setupWithNavController(navController)
    }
}