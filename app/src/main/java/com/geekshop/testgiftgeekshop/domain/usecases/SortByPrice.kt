package com.geekshop.testgiftgeekshop.domain.usecases

import com.geekshop.testgiftgeekshop.domain.MainRepository
import javax.inject.Inject

class SortByPrice @Inject constructor(
    private val repository: MainRepository
) {
    fun execute(maxPrice: Int) {
        repository.filterResultListByPrice(maxPrice)
    }
}