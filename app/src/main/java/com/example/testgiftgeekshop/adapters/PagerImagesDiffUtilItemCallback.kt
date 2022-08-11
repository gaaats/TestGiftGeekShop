package com.example.testgiftgeekshop.adapters

import androidx.recyclerview.widget.DiffUtil
import com.example.testgiftgeekshop.domain.ImageUI

class PagerImagesDiffUtilItemCallback: DiffUtil.ItemCallback<ImageUI>() {
    override fun areItemsTheSame(oldItem: ImageUI, newItem: ImageUI): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: ImageUI, newItem: ImageUI): Boolean {
        return oldItem == newItem
    }
}