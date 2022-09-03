package com.geekshop.testgiftgeekshop.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.size.Scale
import com.geekshop.testgiftgeekshop.R
import com.geekshop.testgiftgeekshop.databinding.ItemVievPagerBinding
import com.geekshop.testgiftgeekshop.domain.ImageUI
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject


class MoreImagesPagerAdapter @Inject constructor() :
    ListAdapter<ImageUI, MoreImagesPagerAdapter.PagerImagesRecVVievHolder>(PagerImagesDiffUtilItemCallback()) {

    inner class PagerImagesRecVVievHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding = ItemVievPagerBinding.bind(view)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PagerImagesRecVVievHolder {
        LayoutInflater.from(parent.context).inflate(R.layout.item_viev_pager, parent, false).also {
            return PagerImagesRecVVievHolder(it)
        }
    }

    override fun onBindViewHolder(holder: PagerImagesRecVVievHolder, position: Int) {
        val currentImage = getItem(position)

        if (currentImage.url.isNullOrEmpty()) {
            holder.binding.imgOnPager.setImageResource(R.drawable.icon_no_pictures)
        } else {
            CoroutineScope(Dispatchers.IO).launch {
                holder.binding.imgOnPager.load(currentImage.url) {
                    placeholder(R.drawable.icon_loading).also {
                        scale(Scale.FIT)
                    }
                }
            }
        }
    }
}