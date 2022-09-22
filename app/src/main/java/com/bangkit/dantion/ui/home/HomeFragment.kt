package com.bangkit.dantion.ui.home

import android.app.Activity
import android.content.Intent
import android.media.AudioRecord
import android.media.MediaPlayer
import android.media.MediaRecorder
import android.net.Uri
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.os.HandlerThread
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.os.HandlerCompat
import androidx.core.view.isInvisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bangkit.dantion.*
import com.bangkit.dantion.data.Result
import com.bangkit.dantion.data.model.Detection
import com.bangkit.dantion.data.model.DetectionStat
import com.bangkit.dantion.databinding.FragmentHomeBinding
import com.bangkit.dantion.ui.allCase.detail.DetailPublicActivity
import com.bangkit.dantion.ui.custom.CustomDialog
import com.bangkit.dantion.ui.notification.NotificationActivity
import com.bangkit.dantion.ui.viewModel.DataStoreViewModel
import com.bangkit.dantion.ui.viewModel.DetectionViewModel
import com.bangkit.dantion.ui.viewModel.ProfileViewModel
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.tensorflow.lite.support.audio.TensorAudio
import org.tensorflow.lite.task.audio.classifier.AudioClassifier
import org.tensorflow.lite.task.audio.classifier.AudioClassifier.AudioClassifierOptions
import org.tensorflow.lite.task.core.BaseOptions
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.concurrent.scheduleAtFixedRate
import kotlin.concurrent.thread
import kotlin.experimental.and


@AndroidEntryPoint
class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var latestDangerAdapter: LatestDangerAdapter

    private val detectionViewModel: DetectionViewModel by viewModels()
    private val dataStoreViewModel: DataStoreViewModel by viewModels()
    private val profileViewModel: ProfileViewModel by viewModels()

    private var latestDetection = ArrayList<Detection>()

    private lateinit var stat: DetectionStat
    private lateinit var token: String
    private lateinit var userId: String
    private lateinit var type: String
    private lateinit var typeDetected: String
    private lateinit var city: String

    private var isCancel = false
    private var isFinished = false
    private var lat: Double = 0.0
    private var lon: Double = 0.0

    private lateinit var countdown: CountDownTimer

    private lateinit var handler: Handler // background thread handler to run classification
    private lateinit var handler2: Handler // background thread handler to run classification
    private var audioClassifier: AudioClassifier? = null
    private var audioRecord: AudioRecord? = null
    private var classificationInterval = 500L // how often should classification run in milli-secs
    private var modelPath = "final-model-metadata.tflite"
    private var probabilityThreshold: Float = 0.01f

    private var dirPath = ""
    private lateinit var recordingFile : File

    private var isRecording = false
    private var recorder : AudioRecord ?= null
    private var recordingThread: Thread? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Create a handler to run classification in a background thread
        val handlerThread = HandlerThread("backgroundThread")
        handlerThread.start()
        handler = HandlerCompat.createAsync(handlerThread.looper)
        val handlerThread2 = HandlerThread("backgroundThread2")
        handlerThread2.start()
        handler2 = HandlerCompat.createAsync(handlerThread2.looper)
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
        binding.btnNotification.setOnClickListener {
            val intent = Intent(activity, NotificationActivity::class.java)
            startActivity(intent)
        }
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
        dataStoreViewModel.getIdUser().observe(viewLifecycleOwner){ idUser ->
            profileViewModel.getUserDetail(token, idUser).observe(viewLifecycleOwner){
                when(it){
                    is Result.Loading -> setLoading(true)
                    is Result.Success ->{
                        setLoading(false)
                        val user = it.data.user
                        val firstName = user.name.toString().getFirstName()
                        userId = user.id.toString()
                        getDetectionStat()
                        Glide.with(requireActivity())
                            .load(user.photo)
                            .placeholder(R.drawable.img_profile)
                            .dontAnimate()
                            .into(binding.ivProfile)
                        binding.tvName.text = getString(R.string.full_name, firstName)
                    }
                    is Result.Error->{
                        setLoading(false)
                        setToastShort(it.error, requireContext())
                    }
                }
            }
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
        detectionViewModel.getUnreadNotification().observe(viewLifecycleOwner){
            binding.tvNotification.text = it.toString()
        }
    }
    private fun getLatestDetection(){
        detectionViewModel.getAllDetections(token)
        detectionViewModel.allDetectionResponse.observe(viewLifecycleOwner){res->
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
    private fun addNewDetection(){
        val requestBodyFile = recordingFile.asRequestBody("audio/mp3".toMediaTypeOrNull())
        val recordingMultipart = MultipartBody.Part.createFormData(
            "recordUrl",
            recordingFile.name,
            requestBodyFile
        )
        detectionViewModel.addNewDetection(
            token=token,
            recordUrl = recordingMultipart,
            userId = userId.toRequestBody("text/plain".toMediaType()),
            type = type.toRequestBody("text/plain".toMediaType()),
            lon = lon.toString().toRequestBody("text/plain".toMediaType()),
            lat = lat.toString().toRequestBody("text/plain".toMediaType()),
            city = city.toRequestBody("text/plain".toMediaType())
        )
    }
    private fun setLatestCase(list: ArrayList<Detection>) {
        latestDangerAdapter = LatestDangerAdapter(list)
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
        val audioTensor = classifier.createInputTensorAudio()

        // Initialize the audio recorder
        val record = classifier.createAudioRecord()
        record.startRecording()
        val filename = "recording-${System.currentTimeMillis()}.wav"

        dirPath = context?.externalCacheDir?.path + "/$filename"

        recorder = AudioRecord(MediaRecorder.AudioSource.MIC,
            RECORDER_SAMPLE_RATE, RECORDER_CHANNELS,
            RECORDER_AUDIO_ENCODING, 512)

        recorder?.startRecording()
        isRecording = true
        recordingFile = File(dirPath)


    val run2 = object : Runnable {
            override fun run() {
                writeAudioDataToFile(dirPath)
            }
        }
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
                                else -> "kebakaran"
                            }
                            typeDetected = outputStr
                            binding.tvLabel.text = type
                        }
                        binding.tvLabel.text = outputStr
                    }
                // Rerun the classification after a certain interval
                handler.postDelayed(this, classificationInterval)
            }
        }

        // Start the classification process
        handler.post(run)
        handler2.post(run2)
    }

    private fun stopAudioClassification() {
        handler.removeCallbacksAndMessages(null)
        handler2.removeCallbacksAndMessages(null)
        audioClassifier = null

        recorder?.run {
            isRecording = false;
            stop()
            release()
            recordingThread = null
            recorder = null
        }
    }
    private fun startEmergency(){
        binding.btnEmergency.setOnLongClickListener{
            showRecordLayout(true)
            isCancel = false
            val recordDuration = 10000L
            startAudioClassification()
            countdown = object : CountDownTimer(recordDuration, 1000) {
                override fun onTick(millisUntilFinished: Long) {
                    if(isCancel) {
                        stopAudioClassification()
                        recordingFile.delete()
                        cancel()
                    }
                    val seconds : Long = (millisUntilFinished / 1000) % 60
                    val minutes = (millisUntilFinished / (1000 * 60)) % 60
                    binding.tvTimeRecord.text = "%02d:%02d".format(minutes, seconds)
                }
                override fun onFinish() {
                    recordingFile = File(dirPath)
                    showRecordLayout(false)
                    stopAudioClassification()
                    addNewDetection()
                    CustomDialog(
                        mainButtonText = getString(R.string.see_record_file),
                        secondaryButtonText = null,
                        title = getString(R.string.success_record, "${type.replaceFirstChar { it.uppercase() }} (${typeDetected.replaceFirstChar { it.uppercase() }})"),
                        subtitle = null,
                        image = null,
                        blockMainButton = {
                            val mediaPlayer = MediaPlayer()
                            mediaPlayer.setDataSource(dirPath)
                            mediaPlayer.prepare()
                            mediaPlayer.start()
                        },
                        blockSecondaryButton = { }
                    ).show(requireActivity().supportFragmentManager, "MyCustomFragment")
                }
            }
            countdown.start()
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
        stopAudioClassification()
        binding.btnHowToUse.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_howToUseFragment)
        }
    }
    private fun showRecordLayout(state: Boolean){
        binding.layoutEmergencyStart.isInvisible = !state
        binding.btnEmergency.isInvisible = state
    }

    private fun short2byte(sData: ShortArray): ByteArray {
        val arrSize = sData.size
        val bytes = ByteArray(arrSize * 2)
        for (i in 0 until arrSize) {
            bytes[i * 2] = (sData[i] and 0x00FF).toByte()
            bytes[i * 2 + 1] = (sData[i].toInt() shr 8).toByte()
            sData[i] = 0
        }
        return bytes
    }

    private fun writeAudioDataToFile(path: String) {
        val sData = ShortArray(BufferElements2Rec)
        var os: FileOutputStream? = null
        try {
            os = FileOutputStream(path)
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        }

        val data = arrayListOf<Byte>()

        for (byte in wavFileHeader()) {
            data.add(byte)
        }

        while (isRecording) {
            // gets the voice output from microphone to byte format
            recorder?.read(sData, 0, BufferElements2Rec)
            try {
                val bData = short2byte(sData)
                for (byte in bData)
                    data.add(byte)
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }

        updateHeaderInformation(data)

        os?.write(data.toByteArray())

        try {
            os?.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    private fun wavFileHeader(): ByteArray {
        val headerSize = 44
        val header = ByteArray(headerSize)

        header[0] = 'R'.toByte() // RIFF/WAVE header
        header[1] = 'I'.toByte()
        header[2] = 'F'.toByte()
        header[3] = 'F'.toByte()

        header[4] = (0 and 0xff).toByte() // Size of the overall file, 0 because unknown
        header[5] = (0 shr 8 and 0xff).toByte()
        header[6] = (0 shr 16 and 0xff).toByte()
        header[7] = (0 shr 24 and 0xff).toByte()

        header[8] = 'W'.toByte()
        header[9] = 'A'.toByte()
        header[10] = 'V'.toByte()
        header[11] = 'E'.toByte()

        header[12] = 'f'.toByte() // 'fmt ' chunk
        header[13] = 'm'.toByte()
        header[14] = 't'.toByte()
        header[15] = ' '.toByte()

        header[16] = 16 // Length of format data
        header[17] = 0
        header[18] = 0
        header[19] = 0

        header[20] = 1 // Type of format (1 is PCM)
        header[21] = 0

        header[22] = NUMBER_CHANNELS.toByte()
        header[23] = 0

        header[24] = (RECORDER_SAMPLE_RATE and 0xff).toByte() // Sampling rate
        header[25] = (RECORDER_SAMPLE_RATE shr 8 and 0xff).toByte()
        header[26] = (RECORDER_SAMPLE_RATE shr 16 and 0xff).toByte()
        header[27] = (RECORDER_SAMPLE_RATE shr 24 and 0xff).toByte()

        header[28] = (BYTE_RATE and 0xff).toByte() // Byte rate = (Sample Rate * BitsPerSample * Channels) / 8
        header[29] = (BYTE_RATE shr 8 and 0xff).toByte()
        header[30] = (BYTE_RATE shr 16 and 0xff).toByte()
        header[31] = (BYTE_RATE shr 24 and 0xff).toByte()

        header[32] = (NUMBER_CHANNELS * BITS_PER_SAMPLE / 8).toByte() //  16 Bits stereo
        header[33] = 0

        header[34] = BITS_PER_SAMPLE.toByte() // Bits per sample
        header[35] = 0

        header[36] = 'd'.code.toByte()
        header[37] = 'a'.code.toByte()
        header[38] = 't'.code.toByte()
        header[39] = 'a'.code.toByte()

        header[40] = (0 and 0xff).toByte() // Size of the data section.
        header[41] = (0 shr 8 and 0xff).toByte()
        header[42] = (0 shr 16 and 0xff).toByte()
        header[43] = (0 shr 24 and 0xff).toByte()

        return header
    }

    private fun updateHeaderInformation(data: ArrayList<Byte>) {
        val fileSize = data.size
        val contentSize = fileSize - 44

        data[4] = (fileSize and 0xff).toByte() // Size of the overall file
        data[5] = (fileSize shr 8 and 0xff).toByte()
        data[6] = (fileSize shr 16 and 0xff).toByte()
        data[7] = (fileSize shr 24 and 0xff).toByte()

        data[40] = (contentSize and 0xff).toByte() // Size of the data section.
        data[41] = (contentSize shr 8 and 0xff).toByte()
        data[42] = (contentSize shr 16 and 0xff).toByte()
        data[43] = (contentSize shr 24 and 0xff).toByte()
    }

    companion object {
        const val RECORDER_SAMPLE_RATE = 8000
        const val RECORDER_CHANNELS: Int = android.media.AudioFormat.CHANNEL_IN_MONO
        const val RECORDER_AUDIO_ENCODING: Int = android.media.AudioFormat.ENCODING_PCM_16BIT
        const val BITS_PER_SAMPLE: Short = 16
        const val NUMBER_CHANNELS: Short = 1
        const val BYTE_RATE = RECORDER_SAMPLE_RATE * NUMBER_CHANNELS * 16 / 8

        var BufferElements2Rec = 1024
    }
}