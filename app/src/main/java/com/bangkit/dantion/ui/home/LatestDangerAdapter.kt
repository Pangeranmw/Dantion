package com.bangkit.dantion.ui.home

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bangkit.dantion.R
import com.bangkit.dantion.data.model.DangerDetection
import com.bangkit.dantion.databinding.LatestCaseItemBinding
import com.bangkit.dantion.getAddress
import com.bangkit.dantion.utils.DangerDetectionDiffCallback

class LatestDangerAdapter(private val list: ArrayList<DangerDetection>, private val activity: Activity) :
    RecyclerView.Adapter<LatestDangerAdapter.ViewHolder>() {

    fun updateData(items: ArrayList<DangerDetection>) {
        val diffCallback = DangerDetectionDiffCallback(list, items)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        list.clear()
        list.addAll(items)
        diffResult.dispatchUpdatesTo(this)
    }

    inner class ViewHolder(private val binding: LatestCaseItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: DangerDetection) {
            checkStatus(data.status,binding)
            binding.tvName.text = data.uId
            binding.tvAddress.text = getAddress(data.latitude, data.longitude, activity.applicationContext)?: activity.applicationContext.getString(R.string.location_address)
            binding.tvDate.text = data.createdAt
            binding.tvStatus.text = data.status
            binding.tvTime.text = activity.applicationContext.getString(R.string.occurance_time, "20","20")
//            itemView.setOnClickListener {
//                val intent = Intent(itemView.context, DetailActivity::class.java)
//                intent.putExtra(DetailActivity.EXTRA_DATA, data.username.toString())
//                activity.startActivity(intent)
//            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LatestCaseItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount(): Int = list.size

    fun checkStatus(status: String, binding: LatestCaseItemBinding){
        if(status == "Pelapor") {
            binding.tvStatus.background = AppCompatResources.getDrawable(activity.applicationContext,R.drawable.rounded_outline_green_thin)
            binding.tvStatus.setTextColor(AppCompatResources.getColorStateList(activity.applicationContext,R.color.green_dark_half))
        }
    }
}
