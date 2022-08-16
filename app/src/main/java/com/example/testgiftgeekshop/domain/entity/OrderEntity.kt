package com.example.testgiftgeekshop.domain.entity

data class OrderEntity(
    val name: String = "",
    val secondName: String = "",
    val deliveryType:DeliveryModel = DeliveryModel(),
    val totalSum:Int = 0,
    val phoneNumberContact:String = "",
    val paymentType:String = "",
){
}