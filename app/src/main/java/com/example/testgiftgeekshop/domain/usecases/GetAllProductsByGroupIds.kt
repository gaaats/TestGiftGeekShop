package com.example.testgiftgeekshop.domain.usecases

import com.example.geekshopappbuy.domain.entitys.GeekProductUI
import com.example.testgiftgeekshop.domain.MainRepository
import com.example.testgiftgeekshop.utils.ResourceVrap
import javax.inject.Inject

class GetAllProductsByGroupIds @Inject constructor(
    private val repository: MainRepository
) {
    suspend fun execute(listParams: List<Int>): ResourceVrap<List<GeekProductUI>> {
        return repository.getAllProductsByGroupIds(listParams)
    }
}