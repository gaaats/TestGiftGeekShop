package com.geekshop.testgiftgeekshop.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.size.Scale
import com.geekshop.geekshopappbuy.domain.entitys.GeekProductUI
import com.geekshop.testgiftgeekshop.R
import com.geekshop.testgiftgeekshop.databinding.SingleItemShopCartBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject


class ShopCartListAdapter @Inject constructor(
) :
    ListAdapter<GeekProductUI, ShopCartListAdapter.ShopCartVievHolder>(ProductDiffUtill()) {

    class ShopCartVievHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val binding = SingleItemShopCartBinding.bind(itemView)
    }

    // todo: make it later
    interface OnItemInsideShopCartNavigation {
    }

    private var onItemPlusClickListener: ((geekProductUI: GeekProductUI) -> Unit)? = null
    private var onItemMinusClickListener: ((geekProductUI: GeekProductUI) -> Unit)? = null
    private var onItemDeleteClickListener: ((geekProductUI: GeekProductUI, countProductPreDelete:Int) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShopCartVievHolder {
        LayoutInflater.from(parent.context)
            .inflate(R.layout.single_item_shop_cart, parent, false).also {
                return ShopCartVievHolder(it)
            }
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ShopCartVievHolder, position: Int) {
        val currentItem = getItem(position)
        holder.binding.apply {
            tvName.text = currentItem.name
            tvPriceItem.text = currentItem.priceForDisplay
            tvItemCount.text = currentItem.countInShopList.toString()
            tvItemSum.text = currentItem.sumForDisplay

            btnPlus.setOnClickListener {
                onItemPlusClickListener?.invoke(currentItem)
                var textInside = tvItemCount.text.toString().toInt()
                var sumInside = tvItemSum.text.toString().replace("₴", "").toInt()
                textInside++
                sumInside += currentItem.price
                tvItemCount.text = textInside.toString()
                tvItemSum.text = "${sumInside}₴"
            }
            btnMinus.setOnClickListener {
                var textInside = tvItemCount.text.toString().toInt()
                var sumInside = tvItemSum.text.toString().replace("₴", "").toInt()
                if (textInside>=2){
                    onItemMinusClickListener?.invoke(currentItem)
                    textInside--
                    sumInside -= currentItem.price
                    tvItemCount.text = textInside.toString()
                    tvItemSum.text = "${sumInside}₴"
                    return@setOnClickListener
                } else {
                    onItemDeleteClickListener?.invoke(currentItem, textInside)
                }
            }
            btnDelete.setOnClickListener {
                val countProductAddedToShopCart = tvItemCount.text.toString().toInt()
                onItemDeleteClickListener?.invoke(currentItem, countProductAddedToShopCart)
            }

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
        }
    }

    fun setOnItemPlusClickListener(listener: (singleProduct: GeekProductUI) -> Unit) {
        onItemPlusClickListener = listener
    }

    fun setOnItemMinusClickListener(listener: (singleProduct: GeekProductUI) -> Unit) {
        onItemMinusClickListener = listener
    }

    fun setOnItemDeleteClickListener(listener: (singleProduct: GeekProductUI, countProductPreDelete:Int) -> Unit) {
        onItemDeleteClickListener = listener
    }
}