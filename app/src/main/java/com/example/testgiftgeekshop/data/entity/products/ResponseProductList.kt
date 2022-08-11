package com.example.geekshopappbuy.data.entity.products


import com.google.gson.annotations.SerializedName

data class ResponseProductList(
    @SerializedName("group_id")
    val groupId: Int?,
    @SerializedName("products")
    val products: List<Product?>?
)