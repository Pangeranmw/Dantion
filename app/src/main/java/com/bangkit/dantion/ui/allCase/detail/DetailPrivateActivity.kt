package com.bangkit.dantion.ui.allCase.detail

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.bangkit.dantion.data.Result
import com.bangkit.dantion.data.local.entity.CaseEntity
import com.bangkit.dantion.data.remote.detection.UpdateDetectionBody
import com.bangkit.dantion.databinding.ActivityDetailPrivateBinding
import com.bangkit.dantion.setToastLong
import com.bangkit.dantion.setToastShort
import com.bangkit.dantion.ui.viewModel.DataStoreViewModel
import com.bangkit.dantion.ui.viewModel.DetectionViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailPrivateActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailPrivateBinding
    private lateinit var updateField: UpdateDetectionBody
    private lateinit var idUser: String
    private lateinit var idCase: String
    private lateinit var token: String
    private lateinit var caseEntity: CaseEntity

    private val dataStoreViewModel: DataStoreViewModel by viewModels()
    private val detectionViewModel: DetectionViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailPrivateBinding.inflate(layoutInflater)
        setContentView(binding.root)

        caseEntity = intent.getParcelableExtra<CaseEntity>(EXTRA_DATA) as CaseEntity

        dataStoreViewModel.getToken().observe(this) {
            token = it.toString()
        }
        dataStoreViewModel.getIdUser().observe(this) {
            idUser = it.toString()
        }

        binding.dangerDetailTitle.setOnClickListener { finish() }
        binding.btnPlay.setOnClickListener { playRecord() }
        binding.btnValidation.setOnClickListener { setStatus(VALIDATION) }
        binding.btnRejection.setOnClickListener { setStatus(REJECTED) }
        binding.btnComplete.setOnClickListener { setStatus(COMPLETE) }
    }

    private fun playRecord() {

    }

    private fun setStatus(status: String) {
        idCase = "asal aja dulu"
        updateField = UpdateDetectionBody(
            id = idCase,
            status = status,
            idUserLogin = idUser
        )

        detectionViewModel.updateDetections(token, updateField).observe(this) { res ->
            when(res){
                is Result.Loading -> setLoading(true)
                is Result.Success -> {
                    setLoading(false)
                    setToastShort(res.data.message, this)
                    finish()
                }
                is Result.Error -> {
                    setLoading(false)
                    setToastLong(res.error, this)
                }
            }
        }
    }

    private fun setLoading(state: Boolean){
        binding.progressBar.visibility = if(state) View.VISIBLE else View.INVISIBLE
    }

    companion object {
        const val VALIDATION = "validasi"
        const val REJECTED = "reject"
        const val COMPLETE = "complete"
        const val EXTRA_DATA = "extra_data"
    }
}