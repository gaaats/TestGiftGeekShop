package com.example.testgiftgeekshop.domain

import com.example.geekshopappbuy.data.entity.products.ResponseProductList
import com.example.geekshopappbuy.data.entity.singleproduct.ResponseSingleProduct
import com.example.geekshopappbuy.domain.entitys.GeekGroupUI
import com.example.geekshopappbuy.domain.entitys.GeekProductUI
import com.example.testgiftgeekshop.utils.AppStatus
import com.example.testgiftgeekshop.utils.ResourceVrap
import com.example.testgiftgeekshop.utils.SimpleResponse
import kotlinx.coroutines.flow.Flow

interface MainRepository {

    suspend fun getProductInfo(productId:Int):SimpleResponse<ResponseSingleProduct>
    suspend fun getAllProductsByIDs(listParams:List<Int>): ResourceVrap<List<GeekProductUI>>
    suspend fun getAllProductsByGroupIds(listGroups:List<Int>): ResourceVrap<List<GeekProductUI>>
    fun filterResultListByPrice(maxPrice: Int)
    fun filterResultListByTitle(params: Int)
}