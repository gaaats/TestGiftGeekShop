package com.geekshop.geekshopappbuy.data.entity.products


import com.google.gson.annotations.SerializedName

data class Category(
    @SerializedName("caption")
    val caption: String?,
    @SerializedName("id")
    val id: Int?
)