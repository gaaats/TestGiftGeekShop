package com.geekshop.testgiftgeekshop.utils

data class ModelAnswer(
    var title: String = "empty",
    var isActive: Boolean = false,
    var codesForSearch: List<Int> = emptyList()
) {
    fun changeState(){
        isActive = !isActive
    }
}