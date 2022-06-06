package com.bangkit.dantion.ui.home

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.bangkit.dantion.data.model.Detection
import com.bangkit.dantion.databinding.ActivityHomeBinding

class HomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding
    private lateinit var latestDangerAdapter: LatestDangerAdapter
    private var dummyDangerDetection= ArrayList<Detection>()
    private var dummyAccDangerDetection= ArrayList<Detection>()
    private var dummyRobDangerDetection= ArrayList<Detection>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
//        dummyDangerDetection.addAll(
//            listOf(
//                Detection("202020",-47.94742,-143.28466,"adadad","Begal","Korban", true,"20 Mei 2022","20:00", "Gatau"),
//                Detection("202020",4.61147,-0.15602,"adadad","Kecelakaan","Pelapor", true,"20 Mei 2022","19:50", "Bebas"),
//                Detection("202020",53.48602,-119.94547,"adadad","Begal","Pelapor", true,"20 Mei 2022","07:00", "Ucup"),
//                Detection("202020",43.99912,1.85398,"adadad","Kecelakaan","Korban", true,"20 Mei 2022","0", "Kakang"),
//                Detection("202020",24.34958,-14.71747,"adadad","Begal","Korban", true,"20 Mei 2022","0", "Asep"),
//                Detection("202020",50.76277,165.59837,"adadad","Begal","Pelapor", true,"20 Mei 2022","0", "Koki"),
//                Detection("202020",20.23162,103.18623,"adadad","Kecelakaan","Pelapor", true,"20 Mei 2022","0", "Udin"),
//            ),
//        )
//        dummyDangerDetection.map {
//            when (it.type) {
//                "Begal" -> dummyRobDangerDetection.add(it)
//                else -> dummyAccDangerDetection.add(it)
//            }
//        }
//        setLatestAcc(dummyAccDangerDetection)
//        setLatestRob(dummyRobDangerDetection)
    }
    private fun setLatestAcc(list: ArrayList<Detection>) {
        latestDangerAdapter = LatestDangerAdapter(list, this)
//        binding.rvLatestAcc.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL,false)
//        binding.rvLatestAcc.adapter = latestDangerAdapter
    }
    private fun setLatestRob(list: ArrayList<Detection>) {
        latestDangerAdapter = LatestDangerAdapter(list, this)
        binding.rvLatestRob.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL,false)
        binding.rvLatestRob.adapter = latestDangerAdapter
    }
}