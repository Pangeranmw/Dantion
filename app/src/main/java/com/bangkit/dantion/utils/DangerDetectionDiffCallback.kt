package com.bangkit.dantion.utils

import androidx.recyclerview.widget.DiffUtil
import com.bangkit.dantion.data.model.DangerDetection

class DangerDetectionDiffCallback(private val mOldFavList: ArrayList<DangerDetection>, private val mNewFavList: ArrayList<DangerDetection>) : DiffUtil.Callback() {
    override fun getOldListSize(): Int {
        return mOldFavList.size
    }

    override fun getNewListSize(): Int {
        return mNewFavList.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return mOldFavList[oldItemPosition].dangerDetectionId == mNewFavList[newItemPosition].dangerDetectionId
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldFavorite = mOldFavList[oldItemPosition]
        val newFavorite = mNewFavList[newItemPosition]
        return oldFavorite.latitude == newFavorite.latitude && oldFavorite.longitude == newFavorite.longitude
    }
}