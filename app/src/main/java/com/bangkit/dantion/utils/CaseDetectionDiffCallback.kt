package com.bangkit.dantion.utils

import androidx.recyclerview.widget.DiffUtil
import com.bangkit.dantion.data.local.entity.CaseEntity

class CaseDetectionDiffCallback(private val mOldFavList: ArrayList<CaseEntity>, private val mNewFavList: ArrayList<CaseEntity>) : DiffUtil.Callback() {
    override fun getOldListSize(): Int {
        return mOldFavList.size
    }
    override fun getNewListSize(): Int {
        return mNewFavList.size
    }
    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return mOldFavList[oldItemPosition].recordUrl == mNewFavList[newItemPosition].recordUrl
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldFavorite = mOldFavList[oldItemPosition]
        val newFavorite = mNewFavList[newItemPosition]
        return oldFavorite.lat == newFavorite.lat && oldFavorite.lon == newFavorite.lon
    }
}