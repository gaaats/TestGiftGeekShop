package com.example.geekshopappbuy.data.entity.products


import com.google.gson.annotations.SerializedName

data class Product(
    @SerializedName("category")
    val category: Category?,
    @SerializedName("currency")
    val currency: String?,
    @SerializedName("description")
    val description: String?,
    @SerializedName("discount")
    val discount: Discount?,
    @SerializedName("external_id")
    val externalId: Any?,
    @SerializedName("group")
    val group: Group?,
    @SerializedName("id")
    val id: Int?,
    @SerializedName("images")
    val images: List<Image?>?,
    @SerializedName("keywords")
    val keywords: String?,
    @SerializedName("main_image")
    val mainImage: String?,
    @SerializedName("minimum_order_quantity")
    val minimumOrderQuantity: Any?,
    @SerializedName("name")
    val name: String?,
    @SerializedName("presence")
    val presence: String?,
    @SerializedName("presence_sure")
    val presenceSure: Boolean?,
    @SerializedName("price")
    val price: Int?,
    @SerializedName("prices")
    val prices: List<Price?>?,
    @SerializedName("selling_type")
    val sellingType: String?,
    @SerializedName("sku")
    val sku: String?,
    @SerializedName("status")
    val status: String?
)