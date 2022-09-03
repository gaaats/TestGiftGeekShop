package com.geekshop.testgiftgeekshop.domain

import com.geekshop.geekshopappbuy.data.entity.singleproduct.ResponseSingleProduct
import com.geekshop.geekshopappbuy.domain.entitys.GeekProductUI
import com.geekshop.testgiftgeekshop.utils.ResourceVrap
import com.geekshop.testgiftgeekshop.utils.SimpleResponse

interface MainRepository {

    suspend fun getProductInfo(productId:Int):SimpleResponse<ResponseSingleProduct>
    suspend fun getAllProductsByIDs(listParams:List<Int>): ResourceVrap<List<GeekProductUI>>
    suspend fun getAllProductsByGroupIds(listGroups:List<Int>): ResourceVrap<List<GeekProductUI>>
    fun filterResultListByPrice(maxPrice: Int)
    fun filterResultListByTitle(params: Int)
}