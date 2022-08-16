package com.example.testgiftgeekshop.domain.entity

data class DeliveryModel(
    val region: String = "",
    val city: String = "",
    val deliveryCompany: DeliveryCompany = NovaPoshta("0"),
    val deliveryPhoneNumber: String = ""
)

sealed class DeliveryCompany(
    deliveryDepartmentNumber: String
)

data class NovaPoshta(val depNumber: String) : DeliveryCompany(deliveryDepartmentNumber = depNumber)
data class UkrPoshta(val indexNumber: String) : DeliveryCompany(deliveryDepartmentNumber = indexNumber)
