package com.example.geekshopappbuy.data.entity.products


import com.google.gson.annotations.SerializedName

data class Group(
    @SerializedName("id")
    val id: Int?,
    @SerializedName("name")
    val name: String?
)