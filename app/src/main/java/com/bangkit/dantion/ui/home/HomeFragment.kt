package com.bangkit.dantion.ui.home

import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isInvisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bangkit.dantion.R
import com.bangkit.dantion.data.Result
import com.bangkit.dantion.data.model.Detection
import com.bangkit.dantion.data.model.DetectionStat
import com.bangkit.dantion.databinding.FragmentHomeBinding
import com.bangkit.dantion.getCity
import com.bangkit.dantion.getFirstName
import com.bangkit.dantion.setToastShort
import com.bangkit.dantion.ui.notification.NotificationActivity
import com.bangkit.dantion.ui.viewModel.DataStoreViewModel
import com.bangkit.dantion.ui.viewModel.DetectionViewModel
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint
import org.tensorflow.lite.task.audio.classifier.AudioClassifier
import java.util.*
import kotlin.concurrent.scheduleAtFixedRate

@AndroidEntryPoint
class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var latestDangerAdapter: LatestDangerAdapter

    private val detectionViewModel: DetectionViewModel by viewModels()
    private val dataStoreViewModel: DataStoreViewModel by viewModels()

    private var latestDetection = ArrayList<Detection>()

    private lateinit var stat: DetectionStat
    private lateinit var token: String
    private lateinit var userId: String
    private lateinit var type: String
    private lateinit var city: String

    private var isCancel = false
    private var lat: Double = 0.0
    private var lon: Double = 0.0

    // TODO 2.1: defines the model to be used
    private var modelPath = "model.tflite"
    // TODO 2.2: defining the minimum threshold
    private var probabilityThreshold: Float = 0.3f

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val view = binding.root
        getToken()
        startEmergency()
        cancelEmergency()
        startHowToUse()
        setNotification()
        return view
    }
    private fun getToken(){
        dataStoreViewModel.getToken().observe(viewLifecycleOwner) { token ->
            this.token = token
//            getLocation()
        }
    }
    private fun getUser(){
        dataStoreViewModel.getUser().observe(viewLifecycleOwner) { user ->
            val firstName = user.name.toString().getFirstName()
            userId = user.id.toString()
            getDetectionStat()
            Glide.with(this)
                .load(user.photo)
                .into(binding.ivProfile)
            binding.tvName.text = getString(R.string.full_name, firstName)
        }
    }
    private fun setNotification() {
        binding.btnNotification.setOnClickListener {
            val intent = Intent(requireActivity(), NotificationActivity::class.java)
            startActivity(intent)
        }
    }
    private fun getLocation(){
        dataStoreViewModel.getLatitude().observe(viewLifecycleOwner) { lat ->
            dataStoreViewModel.getLongitude().observe(viewLifecycleOwner) { lon ->
                this.lat = lat
                this.lon = lon
                city = getCity(lat,lon,requireContext())
                getUser()
            }
        }
    }
    private fun getDetectionStat(){
        detectionViewModel.getDetectionStat().observe(viewLifecycleOwner){res->
            when(res){
                is Result.Loading -> setLoading(true)
                is Result.Success -> {
                    stat = res.data.stat
                    binding.tvStatCrimeNumber.text = stat.kejahatan.toString()
                    binding.tvStatAccNumber.text = stat.kecelakaan.toString()
                    binding.tvStatFireNumber.text = stat.kebakaran.toString()
                }
                is Result.Error -> setLoading(false)
            }
            getLatestDetection()
        }
    }
    private fun getLatestDetection(){
        detectionViewModel.getAllDetections(token).observe(viewLifecycleOwner){res->
            when(res){
                is Result.Loading -> setLoading(true)
                is Result.Success -> {
                    setLoading(false)
                    latestDetection.clear()
                    val nearestDetection = res.data.detections.filter{
                        // get detection only with the same city
                        city.contains(it.city, ignoreCase = true) &&
                                // get detection that only have valid or complete status
                                it.status == "valid" || it.status == "selesai"
                    }.take(5)
                    latestDetection.addAll(nearestDetection)
                    setLatestCase(latestDetection)
                }
                is Result.Error -> {
                    setLoading(false)
                    setToastShort(res.error, requireContext())
                }
            }
        }
    }
//    private fun addNewDetection(){
//        detectionViewModel.addNewDetection(
//            token=token,
//            recordUrl = file.asRequestBody("audio/wav".toMediaTypeOrNull()),
//            userId = userId.toRequestBody("text/plain".toMediaType()),
//            type = type.toRequestBody("text/plain".toMediaType()),
//            lon = lon.toString().toRequestBody("text/plain".toMediaType()),
//            lat = lat.toString().toRequestBody("text/plain".toMediaType()),
//            city = city.toRequestBody("text/plain".toMediaType())
//        )
//    }
    private fun setLatestCase(list: ArrayList<Detection>) {
        latestDangerAdapter = LatestDangerAdapter(list, requireActivity())
        binding.rvLatestCase.layoutManager = LinearLayoutManager(requireContext())
        binding.rvLatestCase.adapter = latestDangerAdapter
    }
    private fun setLoading(state: Boolean){
        binding.progressBar.visibility = if(state) View.VISIBLE else View.INVISIBLE
    }
    private fun startEmergency(){
        binding.btnEmergency.setOnLongClickListener{
            showRecordLayout(true)
//            // TODO 2.3: Loading the model from the assets folder
//            val classifier = AudioClassifier.createFromFile(requireContext(), modelPath)
//
//            // TODO 3.1: Creating an audio recorder
//            val tensor = classifier.createInputTensorAudio()
//
//            // TODO 3.2: showing the audio recorder specification
//            val format = classifier.requiredTensorAudioFormat
////        val recorderSpecs = "Number Of Channels: ${format.channels}\n" +
////                "Sample Rate: ${format.sampleRate}"
////        recorderSpecsTextView.text = recorderSpecs
//
//            // TODO 3.3: Creating
//            val record = classifier.createAudioRecord()
//            record.startRecording()
            object : CountDownTimer(6000, 1000) {
                override fun onTick(millisUntilFinished: Long) {
//                    Timer().scheduleAtFixedRate(1, 500) {
//
//                        // TODO 4.1: Classifing audio data
//                        val numberOfSamples = tensor.load(record)
//                        val output = classifier.classify(tensor)
//
//                        // TODO 4.2: Filtering out classifications with low probability
//                        val filteredModelOutput = output[0].categories.filter {
//                            it.score > probabilityThreshold
//                        }
//
//                        // TODO 4.3: Creating a multiline string with the filtered results
//                        val outputStr =
//                            filteredModelOutput.sortedBy { -it.score }
//                                .joinToString(separator = "\n") { "${it.label} -> ${it.score} " }
//
//                        // TODO 4.4: Updating the UI
//                        if (outputStr.isNotEmpty())
//                            requireActivity().runOnUiThread {
//                                binding.tvLabel.text = outputStr
//                            }
//                    }
                    if(isCancel) onFinish()
                    binding.tvTimeRecord.text = getString(R.string.time_record, (millisUntilFinished / 1000))
                }
                override fun onFinish() {
//                    addNewDetection()
//                    record.stop()
                }
            }.start()
            return@setOnLongClickListener true
        }
    }
    private fun cancelEmergency(){
        binding.btnCancelRecord.setOnClickListener {
            isCancel = true
            showRecordLayout(false)
        }
    }
    private fun startHowToUse(){
        binding.btnHowToUse.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_howToUseFragment)
        }
    }
    private fun showRecordLayout(state: Boolean){
        binding.layoutEmergencyStart.isInvisible = !state
        binding.btnEmergency.isInvisible = state
    }
}