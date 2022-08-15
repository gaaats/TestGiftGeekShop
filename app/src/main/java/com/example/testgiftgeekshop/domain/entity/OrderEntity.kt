package com.example.testgiftgeekshop.domain.entity

data class OrderEntity(
    val name: String,
    val secondName: String,
    val deliveryType:DeliveryModel,
    val totalSum:Int,
    val phoneNumberContact:String
)