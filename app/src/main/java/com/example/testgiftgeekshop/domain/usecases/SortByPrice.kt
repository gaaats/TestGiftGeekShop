package com.example.testgiftgeekshop.domain.usecases

import com.example.testgiftgeekshop.domain.MainRepository
import javax.inject.Inject

class SortByPrice @Inject constructor(
    private val repository: MainRepository
) {
    suspend fun execute(maxPrice: Int) {
        repository.filterResultListByPrice(maxPrice)
    }
}