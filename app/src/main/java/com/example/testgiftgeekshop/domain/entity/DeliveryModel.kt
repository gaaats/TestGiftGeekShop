package com.example.testgiftgeekshop.domain.entity

data class DeliveryModel(
    val region: String,
    val city: String,
    val type: DeliveryCompany,
    val deliveryPhoneNumber: String
)

sealed class DeliveryCompany(
    deliveryDepartmentNumber: Int
)

data class NovaPoshta(val depNumber: Int) : DeliveryCompany(deliveryDepartmentNumber = depNumber)
data class UkrPoshta(val indexNumber: Int) : DeliveryCompany(deliveryDepartmentNumber = indexNumber)
