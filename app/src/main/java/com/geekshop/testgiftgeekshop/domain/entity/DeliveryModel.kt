package com.geekshop.testgiftgeekshop.domain.entity

data class DeliveryModel(
    val region: String = "",
    val city: String = "",
    val deliveryCompany: DeliveryCompany = NovaPoshta(),
    val deliveryPhoneNumber: String = ""
) {
    val getDeliveryServiceName: String
        get() = deliveryCompany.name

    val getDeliveryDepartmentNumber: String
        get() = deliveryCompany.number

}

sealed class DeliveryCompany(
    deliveryDepartmentNumber: String,
    deliveryServiceName: String
) {
    val name = deliveryServiceName
    val number = deliveryDepartmentNumber
}

data class NovaPoshta(
    val depNumber: String = "0",
    val deliveryServiceName1: String = "Нова пошта"
) :
    DeliveryCompany(
        deliveryDepartmentNumber = depNumber,
        deliveryServiceName = deliveryServiceName1
    )

data class UkrPoshta(val indexNumber: String = "0", val deliveryServiceName1: String = "Укрпошта") :
    DeliveryCompany(
        deliveryDepartmentNumber = indexNumber,
        deliveryServiceName = deliveryServiceName1
    )
