package com.example.geekshopappbuy.data.entity.singleproduct


import com.example.geekshopappbuy.data.entity.products.Product
import com.google.gson.annotations.SerializedName

data class ResponseSingleProduct(
    @SerializedName("product")
    val product: Product?
)