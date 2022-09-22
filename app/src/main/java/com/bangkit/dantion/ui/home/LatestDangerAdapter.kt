package com.bangkit.dantion.ui.home

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bangkit.dantion.*
import com.bangkit.dantion.data.model.Detection
import com.bangkit.dantion.databinding.LatestCaseItemBinding
import com.bangkit.dantion.ui.allCase.detail.DetailPrivateActivity.Companion.EXTRA_DATA
import com.bangkit.dantion.ui.allCase.detail.DetailPublicActivity
import com.bangkit.dantion.utils.DangerDetectionDiffCallback

class LatestDangerAdapter(private val list: ArrayList<Detection>) :
    RecyclerView.Adapter<LatestDangerAdapter.ViewHolder>() {

//    fun updateData(items: ArrayList<Detection>) {
//        val diffCallback = DangerDetectionDiffCallback(list, items)
//        val diffResult = DiffUtil.calculateDiff(diffCallback)
//        list.clear()
//        list.addAll(items)
//        diffResult.dispatchUpdatesTo(this)
//    }

    inner class ViewHolder(private val binding: LatestCaseItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: Detection) {
            binding.tvName.text = data.name.replaceFirstChar { it.uppercase() }
            binding.tvAddress.text = getAddress(data.lat, data.lon, itemView.context)?: itemView.context.getString(R.string.location_unknown)
            binding.tvDate.text = data.updatedAt.getDateFromTimeStamp().withDateFormat()
            binding.tvTime.text = data.updatedAt.getTimeFromTimeStamp().withTimeFormat()
            binding.tvType.text = data.type.replaceFirstChar { it.uppercase() }
            itemView.setOnClickListener {
                val intent = Intent(itemView.context, DetailPublicActivity::class.java)
                intent.putExtra(DetailPublicActivity.EXTRA_DATA, data)
                itemView.context.startActivity(intent)
            }
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
}
