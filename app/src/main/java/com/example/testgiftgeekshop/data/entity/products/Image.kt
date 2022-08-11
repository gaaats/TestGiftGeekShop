package com.example.geekshopappbuy.data.entity.products


import com.google.gson.annotations.SerializedName

data class Image(
    @SerializedName("id")
    val id: Any?,
    @SerializedName("thumbnail_url")
    val thumbnailUrl: String?,
    @SerializedName("url")
    val url: String?
)