package com.geekshop.testgiftgeekshop.domain.entity

data class OrderEntity(
    val name: String = "",
    val secondName: String = "",
    val deliveryType: DeliveryModel = DeliveryModel(),
    val totalSum: Int = 0,
    val phoneNumberContact: String = "",
    val paymentType: String = "",
) {
    fun returnTextAboutDelivery() = "Дані для доставки:\n" +
            "Область: ${deliveryType.region}\n"+
            "Місто: ${deliveryType.city} \n" +
            "Служба доставки: ${deliveryType.getDeliveryServiceName} \n" +
            "Відділення для доставки: ${deliveryType.getDeliveryDepartmentNumber} \n" +
            "Оплата буде: $paymentType \n" +
            "Номер телефона $phoneNumberContact \n" +
            "Ім'я одержувача $name \n" +
            "Прізвище одержувача $secondName"
}