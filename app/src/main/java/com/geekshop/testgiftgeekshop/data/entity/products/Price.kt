package com.geekshop.geekshopappbuy.data.entity.products


import com.google.gson.annotations.SerializedName

data class Price(
    @SerializedName("minimum_order_quantity")
    val minimumOrderQuantity: Int?,
    @SerializedName("price")
    val price: Double?
)