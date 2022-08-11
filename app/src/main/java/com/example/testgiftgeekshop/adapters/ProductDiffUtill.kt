package com.example.testgiftgeekshop.adapters
import androidx.recyclerview.widget.DiffUtil
import com.example.geekshopappbuy.domain.entitys.GeekProductUI

class ProductDiffUtill: DiffUtil.ItemCallback<GeekProductUI>() {
    override fun areItemsTheSame(oldItem: GeekProductUI, newItem: GeekProductUI): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: GeekProductUI, newItem: GeekProductUI): Boolean {
        return oldItem == newItem
    }
}