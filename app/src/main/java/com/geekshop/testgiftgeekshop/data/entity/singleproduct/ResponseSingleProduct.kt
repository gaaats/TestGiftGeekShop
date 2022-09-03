package com.geekshop.geekshopappbuy.data.entity.singleproduct


import com.geekshop.geekshopappbuy.data.entity.products.Product
import com.google.gson.annotations.SerializedName

data class ResponseSingleProduct(
    @SerializedName("product")
    val product: Product?
)