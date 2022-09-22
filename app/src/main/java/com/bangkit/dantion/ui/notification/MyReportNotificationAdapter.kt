package com.bangkit.dantion.ui.notification

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bangkit.dantion.*
import com.bangkit.dantion.data.local.entity.MyDetectionReportEntity
import com.bangkit.dantion.databinding.NotificationItemBinding
import com.bangkit.dantion.utils.MyDetectionReportDiffCallback

class MyReportNotificationAdapter(private val myReportList: ArrayList<MyDetectionReportEntity>, private val onClick : (MyDetectionReportEntity) -> Unit) :
    RecyclerView.Adapter<MyReportNotificationAdapter.ViewHolder>() {

    fun updateData(items: ArrayList<MyDetectionReportEntity>) {
        val diffCallback = MyDetectionReportDiffCallback(myReportList, items)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        myReportList.clear()
        myReportList.addAll(items)
        diffResult.dispatchUpdatesTo(this)
    }

    inner class ViewHolder(private val binding: NotificationItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: MyDetectionReportEntity) {
            val context = itemView.context
            binding.tvAddress.text = data.address
            binding.tvTime.text = getTimeDifference(data.createdAt)
            binding.tvLocation.text = data.city
            binding.tvType.text = data.type.replaceFirstChar { it.uppercase() }
            setStatus(binding, data, context)
            itemView.setOnClickListener {
                onClick(data)
            }
        }
    }
    private fun setStatus(binding: NotificationItemBinding, data: MyDetectionReportEntity, context: Context){
        val role = when(data.type) {
            "kejahatan" -> "Polisi"
            "kecelakaan" -> "Tenaga Medis"
            else -> "Damkar"
        }
        when(data.status){
            "invalid" -> {
                binding.tvStatus.text = context.getString(R.string.notification_status_invalid, role)
                binding.tvStatus.setTextColor(context.getColor(R.color.yellow_dark_full))
                binding.tvStatus.background = AppCompatResources.getDrawable(context,R.drawable.rounded_yellow_light_half)
            }
            "valid" -> {
                binding.tvStatus.text = context.getString(R.string.notification_status_valid,role)
                binding.tvStatus.setTextColor(context.getColor(R.color.green_dark_full))
                binding.tvStatus.background = AppCompatResources.getDrawable(context,R.drawable.rounded_green_light_half)
            }
            "selesai" -> {
                binding.tvStatus.text = context.getString(R.string.notification_status_selesai)
                binding.tvStatus.setTextColor(context.getColor(R.color.green_dark_full))
                binding.tvStatus.background = AppCompatResources.getDrawable(context,R.drawable.rounded_green_light_full)
            }
            else -> {
                binding.tvStatus.text = context.getString(R.string.notification_status_ditolak, role)
                binding.tvStatus.setTextColor(context.getColor(R.color.primary_dark_full))
                binding.tvStatus.background = AppCompatResources.getDrawable(context,R.drawable.rounded_primary_light_half)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = NotificationItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(myReportList[position])
    }

    override fun getItemCount(): Int = myReportList.size
}
