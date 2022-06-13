package com.bangkit.dantion.ui.profile.password

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doAfterTextChanged
import com.bangkit.dantion.R
import com.bangkit.dantion.checkPassword
import com.bangkit.dantion.data.Result
import com.bangkit.dantion.data.remote.user.UpdatePasswordBody
import com.bangkit.dantion.databinding.ActivityPasswordBinding
import com.bangkit.dantion.setToastLong
import com.bangkit.dantion.setToastShort
import com.bangkit.dantion.ui.viewModel.DataStoreViewModel
import com.bangkit.dantion.ui.viewModel.ProfileViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PasswordActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPasswordBinding
    private val profileViewModel: ProfileViewModel by viewModels()
    private val dataStoreViewModel: DataStoreViewModel by viewModels()
    private lateinit var passField: UpdatePasswordBody

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.etPasswordOld.doAfterTextChanged { text->
            val txt = text.toString()
            checkPassword(txt, binding.etPasswordOldLayout, this)
        }
        binding.etPasswordNew.doAfterTextChanged { text->
            val txt = text.toString()
            checkPassword(txt, binding.etPasswordNewLayout, this)
        }
        binding.etPasswordNewConfirm.doAfterTextChanged { text->
            val txt = text.toString()
            checkPassword(txt, binding.etPasswordNewConfirmLayout, this)
        }

        binding.changePasswordTitle.setOnClickListener { finish() }
        binding.btnSave.setOnClickListener {
            if(isFilled()) {
                if(isPasswordSame()) saveData()
                else showToast(R.string.password_same)
            }
            else showToast(R.string.empty_information_account)
        }
    }

    private fun saveData() {
        var token = ""
        var id = ""
        dataStoreViewModel.getToken().observe(this) {
            token = it.toString()
        }
        dataStoreViewModel.getIdUser().observe(this) {
            id = it.toString()
        }

        passField = UpdatePasswordBody(
            id = id,
            password = binding.etPasswordOld.text.toString(),
            newPassword = binding.etPasswordNewConfirm.text.toString()
        )

        profileViewModel.updatePassword(token, passField).observe(this) { res ->
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

    private fun isFilled(): Boolean{
        val passOld = binding.etPasswordOld.text.toString()
        val passNew = binding.etPasswordNew.text.toString()
        val passNewConf = binding.etPasswordNewConfirm.text.toString()

        return passOld.isNotEmpty() && passNew.isNotEmpty() && passNewConf.isNotEmpty() &&
                passOld.length > 5 && passNew.length > 5 && passNewConf.length > 5
    }

    private fun isPasswordSame(): Boolean{
        return binding.etPasswordNew.text.toString() == binding.etPasswordNewConfirm.text.toString()
    }

    private fun showToast(text: Int) {
        Toast.makeText(this, getString(text), Toast.LENGTH_LONG).show()
    }

    private fun setLoading(state: Boolean){
        binding.progressBar.visibility = if(state) View.VISIBLE else View.INVISIBLE
    }
}