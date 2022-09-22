package com.bangkit.dantion.ui.profile

import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.widget.doAfterTextChanged
import com.bangkit.dantion.*
import com.bangkit.dantion.data.Result
import com.bangkit.dantion.data.model.LoginResult
import com.bangkit.dantion.data.remote.user.UpdateUserBody
import com.bangkit.dantion.databinding.ActivityUpdateUserBinding
import com.bangkit.dantion.ui.viewModel.DataStoreViewModel
import com.bangkit.dantion.ui.viewModel.ProfileViewModel
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.io.InputStream

@AndroidEntryPoint
class UpdateUserActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUpdateUserBinding
    private val profileViewModel: ProfileViewModel by viewModels()
    private val dataStoreViewModel: DataStoreViewModel by viewModels()
    private lateinit var userField: UpdateUserBody
    private lateinit var token: String
    private lateinit var id: String
    private var getFile: File? = null
    private var isReduce: Boolean = false
    private var isPhotoChanged: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUpdateUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initData()

        binding.etAddress.doAfterTextChanged { text->
            val txt = text.toString()
            checkPassword(txt, binding.etAddressLayout, this)
        }
        binding.etEmail.doAfterTextChanged { text->
            val txt = text.toString()
            checkEmail(txt, binding.etEmailLayout, this)
        }
        binding.etParentNumber.doAfterTextChanged { text->
            val txt = text.toString()
            checkNumber(txt, binding.etParentNumberLayout, this)
        }
        binding.etNumber.doAfterTextChanged { text->
            val txt = text.toString()
            checkNumber(txt, binding.etNumberLayout, this)
        }
        binding.etName.doAfterTextChanged { text->
            val txt = text.toString()
            emptyData(txt, binding.etNameLayout, this)
        }

        binding.changeProfile.setOnClickListener { finish() }
        binding.profileLayout.setOnClickListener { startGallery() }
        binding.btnSave.setOnClickListener {
            if(isFilled()) saveData()
            else showToast(R.string.empty_information_account)
        }
    }

    private fun startGallery() {
        binding.btnSave.isEnabled = false
        val intent = Intent()
        intent.action = Intent.ACTION_GET_CONTENT
        intent.type = "image/*"
        val chooser = Intent.createChooser(intent, getString(R.string.choose_location))
        isPhotoChanged = true
        launcherIntentGallery.launch(chooser)
    }
    private val launcherIntentGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val selectedImg: Uri = result.data?.data as Uri
            val myFile = uriToFile(selectedImg, this)
            reduceFileAsync(myFile)
            isReduce = true
            val imageStream = contentResolver.openInputStream(
                selectedImg
            ) as InputStream
            val image = BitmapFactory.decodeStream(imageStream)
//            binding.ivProfile.setImageBitmap(cropImage(image))
            binding.ivProfile.setImageBitmap(image)
        }
    }

    private fun initData(){
        dataStoreViewModel.getToken().observe(this){token->
            dataStoreViewModel.getIdUser().observe(this){ idUser ->
                profileViewModel.getUserDetail(token, idUser).observe(this){
                    when(it){
                        is Result.Loading -> setLoading(true)
                        is Result.Success ->{
                            setLoading(false)
                            val user = it.data.user
                            id = user.id.toString()
                            Glide.with(this)
                                .load(user.photo)
                                .placeholder(R.drawable.img_profile)
                                .dontAnimate()
                                .into(binding.ivProfile)
                            binding.apply {
                                etName.setText(user.name.toString())
                                etAddress.setText(user.address.toString())
                                etParentNumber.setText(user.parentNumber.toString())
                                etNumber.setText(user.number.toString())
                                etEmail.setText(user.email.toString())
                            }
                        }
                        is Result.Error->{
                            setLoading(false)
                            setToastShort(it.error, this)
                        }
                    }
                }
            }
        }
    }
    private fun saveData() {
        userField = UpdateUserBody(
            id = id,
            name = binding.etName.text.toString(),
            address = binding.etAddress.text.toString(),
            parentNumber = binding.etParentNumber.text.toString(),
            number = binding.etNumber.text.toString(),
            email = binding.etEmail.text.toString(),
        )
        dataStoreViewModel.getToken().observe(this){
            profileViewModel.updateUser(it, userField).observe(this) { res ->
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
        if(isPhotoChanged) uploadImage()
    }
    private fun uploadImage(){
        if (getFile != null) {
            val file = getFile as File
            val requestImageFile = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
            val imageMultipart = MultipartBody.Part.createFormData(
                "photo",
                file.name,
                requestImageFile
            )
            dataStoreViewModel.getToken().observe(this){
                profileViewModel.updateUserPhoto(it, id, imageMultipart).observe(this) { response ->
                    when(response) {
                        is Result.Loading -> setLoading(true)
                        is Result.Success -> {
                            setLoading(false)
                        }
                        is Result.Error -> {
                            setLoading(false)
                            setToastLong(response.error, this)
                        }
                    }
                }
            }
        }else{
            if(isReduce) uploadImage()
        }
    }
    private fun reduceFileAsync(myFile: File){
        setLoading(true)
        val imageSize = getImageSize(BitmapFactory.decodeFile(myFile.path),100)
        if(imageSize>1000000) showToast(R.string.loading_reduce_message)
        CoroutineScope(Dispatchers.Default).launch {
            getFile = reduceFileImage(myFile)
            launch(Dispatchers.Main) { setLoading(false) }
            if(imageSize<1000000) launch(Dispatchers.Main) { showToast(R.string.success_reduce_message) }
            launch(Dispatchers.Main) { binding.btnSave.isEnabled = true }
        }
    }
    private fun isFilled(): Boolean{
        val name = binding.etName.text.toString()
        val address = binding.etAddress.text.toString()
        val parentNumber = binding.etParentNumber.text.toString()
        val number = binding.etNumber.text.toString()
        val email = binding.etEmail.text.toString()

        return name.isNotEmpty() && address.isNotEmpty() && parentNumber.isNotEmpty() && email.isNotEmpty() && number.isNotEmpty()
    }

    private fun showToast(text: Int) {
        Toast.makeText(this, getString(text), Toast.LENGTH_LONG).show()
    }

    private fun setLoading(state: Boolean){
        binding.progressBar.visibility = if(state) View.VISIBLE else View.INVISIBLE
    }
}