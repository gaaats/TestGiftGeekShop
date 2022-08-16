package com.example.testgiftgeekshop.data.entity

data class ErrorInputModel(
    var errorFirstName: Boolean = false,
    var errorSecondName: Boolean = false,
    var errorCity: Boolean = false,
    var errorPhoneNumber: Boolean = false,
    var errorDeliveryDepartmentNumber: Boolean = false,
) {
    fun cleanAllErrors() {
        errorFirstName = false
        errorSecondName = false
        errorCity = false
        errorPhoneNumber = false
        errorDeliveryDepartmentNumber = false
    }

    fun isThereAnyError(): Boolean {
        if (errorFirstName || errorSecondName || errorCity || errorPhoneNumber || errorDeliveryDepartmentNumber) return true
        return false
    }
}