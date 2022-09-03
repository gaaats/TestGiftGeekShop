package com.geekshop.testgiftgeekshop.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import coil.load
import coil.size.Scale
import com.geekshop.geekshopappbuy.domain.entitys.GeekProductUI
import com.geekshop.testgiftgeekshop.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class ProductsListAdapter @Inject constructor(
) :
    ListAdapter<GeekProductUI, ProductsRecVVievHolder>(ProductDiffUtill()) {

    private var onItemClickListener: ((groupId: Int) -> Unit)? = null
    private var onItemAddToCartClickListener: ((singleProduct: GeekProductUI) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductsRecVVievHolder {
        LayoutInflater.from(parent.context)
            .inflate(R.layout.single_item_goods, parent, false).also {
                return ProductsRecVVievHolder(it)
            }
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ProductsRecVVievHolder, position: Int) {
        val currentItem = getItem(position)
        holder.binding.apply {
            tvName.text = currentItem.name
            tvPrice.text = "${currentItem.price} ₴"
            if (currentItem.mainImage.isEmpty()) {
                imgPhoto.setImageResource(R.drawable.icon_loading)
            } else {
                CoroutineScope(Dispatchers.IO).launch {
                    imgPhoto.load(currentItem.mainImage) {
                        placeholder(R.drawable.icon_loading).also {
                            scale(Scale.FIT)
                        }
                    }
                }
            }
            btnAddToShopCart.setOnClickListener {
                onItemAddToCartClickListener?.invoke(currentItem)
            }
            imgPhoto.setOnClickListener {
                onItemClickListener?.invoke(currentItem.id)
            }
        }
    }

    fun setOnItemClickListener(listener: (groupId: Int) -> Unit) {
        onItemClickListener = listener
    }

    fun setOnItemAddToCartClickListener(listener: (singleProduct: GeekProductUI) -> Unit) {
        onItemAddToCartClickListener = listener
    }
}