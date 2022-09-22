package com.bangkit.dantion.utils

import androidx.recyclerview.widget.DiffUtil
import com.bangkit.dantion.data.local.entity.DetectionReportEntity

class DetectionReportDiffCallback(private val mOldFavList: ArrayList<DetectionReportEntity>, private val mNewFavList: ArrayList<DetectionReportEntity>) : DiffUtil.Callback() {
    override fun getOldListSize(): Int {
        return mOldFavList.size
    }
    override fun getNewListSize(): Int {
        return mNewFavList.size
    }
    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return mOldFavList[oldItemPosition].detectionId == mNewFavList[newItemPosition].detectionId
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldFavorite = mOldFavList[oldItemPosition]
        val newFavorite = mNewFavList[newItemPosition]
        return oldFavorite.address == newFavorite.address && oldFavorite.type == newFavorite.type
    }
}