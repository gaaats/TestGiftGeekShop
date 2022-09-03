package com.geekshop.testgiftgeekshop.domain.usecases

import com.geekshop.geekshopappbuy.domain.entitys.GeekProductUI
import com.geekshop.testgiftgeekshop.domain.MainRepository
import com.geekshop.testgiftgeekshop.utils.ResourceVrap
import javax.inject.Inject

class GetAllProductsByIDs @Inject constructor(
    private val repository: MainRepository
) {
    suspend fun execute(listParams: List<Int>): ResourceVrap<List<GeekProductUI>> {
        return repository.getAllProductsByIDs(listParams)
    }
}