package com.bangkit.dantion.ui.allCase

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bangkit.dantion.*
import com.bangkit.dantion.data.local.AppDatabase
import com.bangkit.dantion.data.local.entity.CaseEntity
import com.bangkit.dantion.data.mapper.caseEntityToDetection
import com.bangkit.dantion.data.model.Detection
import com.bangkit.dantion.databinding.CaseItemBinding
import com.bangkit.dantion.ui.allCase.detail.DetailPublicActivity
import com.bangkit.dantion.ui.viewModel.DetectionViewModel
import com.bangkit.dantion.utils.CaseDetectionDiffCallback
import com.bumptech.glide.Glide

class DangerCaseAdapter(private val list: ArrayList<CaseEntity>, private val activity: Activity):

    RecyclerView.Adapter<DangerCaseAdapter.ViewHolder>() {
    fun updateData(items: ArrayList<CaseEntity>) {
        val diffCallback = CaseDetectionDiffCallback(list, items)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        list.clear()
        list.addAll(items)
        diffResult.dispatchUpdatesTo(this)
    }

    inner class ViewHolder(private val binding: CaseItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: CaseEntity) {
            setStatus(binding, data)
            binding.tvName.text = data.name.getFirstName()
            binding.tvAddress.text = getAddress(data.lat, data.lon, activity.applicationContext)?: activity.applicationContext.getString(R.string.location_unknown)
            binding.tvDate.text = data.updatedAt.getDateFromTimeStamp().withDateFormat()
            binding.tvTime.text = data.updatedAt.getTimeFromTimeStamp().withTimeFormat()
            binding.tvType.text = data.type.replaceFirstChar { it.uppercase() }
            Glide.with(activity)
                .load(data.photo)
                .into(binding.ivProfile)
            itemView.setOnClickListener {
                val intent = Intent(itemView.context, DetailPublicActivity::class.java)
                intent.putExtra(DetailPublicActivity.EXTRA_DATA, caseEntityToDetection(data))
                activity.startActivity(intent)
            }
        }
    }
    private fun setStatus(binding: CaseItemBinding, data: CaseEntity){
        when(data.status){
            "selesai" -> binding.tvStatus.background = AppCompatResources.getDrawable(activity,R.drawable.rounded_green_light_full)
            else -> binding.tvStatus.background = AppCompatResources.getDrawable(activity,R.drawable.rounded_green_light_half)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = CaseItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount(): Int = list.size
}
