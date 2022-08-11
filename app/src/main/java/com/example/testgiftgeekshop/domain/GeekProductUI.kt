package com.example.geekshopappbuy.domain.entitys

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
    val images: List<String>
)