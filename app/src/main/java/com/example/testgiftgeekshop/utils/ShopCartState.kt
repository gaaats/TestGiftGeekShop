package com.example.testgiftgeekshop.utils

import com.example.geekshopappbuy.domain.entitys.GeekProductUI

data class ShopCartState(
    val list: MutableList<GeekProductUI> = mutableListOf(),
    var totalValue: Int = 0
) {
    val totalSumForDisplay = getTotalSum()

    private fun getTotalSum(): String {
        val result = list.sumOf {
            it.price
        }
        totalValue = result
        return "${totalValue}uah"
    }
}