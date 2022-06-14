package com.bangkit.dantion.ui.home

import android.media.AudioRecord
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.os.HandlerThread
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.HandlerCompat
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
import com.bangkit.dantion.ui.viewModel.DataStoreViewModel
import com.bangkit.dantion.ui.viewModel.DetectionViewModel
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import org.tensorflow.lite.support.audio.TensorAudio
import org.tensorflow.lite.task.audio.classifier.AudioClassifier
import org.tensorflow.lite.task.audio.classifier.AudioClassifier.AudioClassifierOptions
import org.tensorflow.lite.task.core.BaseOptions
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
    private var isFinished = false
    private var lat: Double = 0.0
    private var lon: Double = 0.0

    private lateinit var timer: TimerTask
    private lateinit var countdown: CountDownTimer

    private lateinit var handler: Handler // background thread handler to run classification
    private var audioClassifier: AudioClassifier? = null
    private var audioRecord: AudioRecord? = null
    private var classificationInterval = 1000L // how often should classification run in milli-secs

    private var modelPath = "final-model-metadata.tflite"
    private var probabilityThreshold: Float = 0.05f

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Create a handler to run classification in a background thread
        val handlerThread = HandlerThread("backgroundThread")
        handlerThread.start()
        handler = HandlerCompat.createAsync(handlerThread.looper)
    }
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
        seeAllCase()
        return view
    }
    private fun seeAllCase(){
        binding.btnAllCase.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_caseFragment)
        }
    }
    private fun getToken(){
        dataStoreViewModel.getToken().observe(viewLifecycleOwner) { token ->
            this.token = token
            getLocation()
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
                        (it.status == "valid" || it.status == "selesai")
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
    private fun startAudioClassification() {
        // If the audio classifier is initialized and running, do nothing.
        if (audioClassifier != null) return;

        // Initialize the audio classifier
        val options = AudioClassifierOptions.builder()
            .setBaseOptions(BaseOptions.builder().useGpu().build())
            .setMaxResults(1)
            .build()
        val classifier = AudioClassifier.createFromFileAndOptions(context, modelPath, options)
//        val classifier = AudioClassifier.createFromFile(context, modelPath)
        val audioTensor = classifier.createInputTensorAudio()

        // Initialize the audio recorder
        val record = classifier.createAudioRecord()
        record.startRecording()

        // Define the classification runnable
        val run = object : Runnable {
            override fun run() {
                // Load the latest audio sample
                audioTensor.load(record)
                val output = classifier.classify(audioTensor)
                val filteredModelOutput = output[0].categories.filter {
                    it.score > probabilityThreshold
                }
                val outputStr =
                    filteredModelOutput.sortedBy { -it.score }
                        .joinToString(separator = "\n") { "${it.label} -> ${it.score} " }
                Log.d("label", filteredModelOutput.toString())
                if (outputStr.isNotEmpty())
                    requireActivity().runOnUiThread {
                        if(!outputStr.contains("random", true)) {
                            type = when{
                                outputStr.contains("begal", true) ||
                                        outputStr.contains("maling", true) ||
                                        outputStr.contains("pencuri", true) ||
                                        outputStr.contains("rampok", true)
                                -> "kejahatan"
                                outputStr.contains("kecelakaan", true) ||
                                        outputStr.contains("tabrakan", true)
                                -> "kecelakaan"
                                outputStr.contains("kebakaran", true)
                                -> "kebakaran"
                                else -> "random"
                            }
//                            binding.tvLabel.text = type
//                            stopAudioClassification()
                        }
                        binding.tvLabel.text = outputStr
                    }
                // Rerun the classification after a certain interval
                handler.postDelayed(this, classificationInterval)
            }
        }

        // Start the classification process
        handler.post(run)

        // Save the instances we just created for use later
        audioClassifier = classifier
        audioRecord = record
    }

    private fun stopAudioClassification() {
        handler.removeCallbacksAndMessages(null)
        audioRecord?.stop()
        audioRecord = null
        audioClassifier = null
    }
    private fun startEmergency(){
        binding.btnEmergency.setOnLongClickListener{
            showRecordLayout(true)
            isCancel = false
            val recordDuration = 90000L
            startAudioClassification()
            countdown = object : CountDownTimer(recordDuration, 1000) {
                override fun onTick(millisUntilFinished: Long) {
                    if(isCancel) {
                        stopAudioClassification()
                        cancel()
                    }
                    val seconds : Long = (millisUntilFinished / 1000) % 60
                    val minutes = (millisUntilFinished / (1000 * 60)) % 60
//                    getString(R.string.time_record, (millisUntilFinished / 1000), 0, 0, 0)
                    binding.tvTimeRecord.text = "%02d:%02d".format(minutes, seconds)
                }
                override fun onFinish() {
                    isFinished = true
//                    addNewDetection()
                    showRecordLayout(false)
                    stopAudioClassification()
                }
            }
            if(!isFinished) countdown.start()
            return@setOnLongClickListener true
        }
    }
    private fun cancelEmergency(){
        binding.btnCancelRecord.setOnClickListener {
            isCancel = true
            isFinished = false
            showRecordLayout(false)
        }
    }
    private fun startHowToUse(){
        stopAudioClassification()
        binding.btnHowToUse.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_howToUseFragment)
        }
    }
    private fun showRecordLayout(state: Boolean){
        binding.layoutEmergencyStart.isInvisible = !state
        binding.btnEmergency.isInvisible = state
    }
}