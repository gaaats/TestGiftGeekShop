package com.example.geekshopappbuy.domain.entitys

import com.example.geekshopappbuy.data.entity.products.Image
import com.example.testgiftgeekshop.domain.ImageUI

data class GeekProductUI(
    val id: Int,
    val name: String?,
    val price: Int,
    val groupId: Int,
    val group_Name: String,
    val category: String,
    val description: String,
    val mainImage: String,
    val status: String,
    val presence: String,
    val images: List<ImageUI>,
    var isElementInShopList: Boolean = false,
    var countInShopList: Int = 1,
    var sumInShopList: Int = countInShopList * price,
) {
    val priceForDisplay
        get() = "$price₴"

    val sumForDisplay
        get() = "$sumInShopList₴"
}