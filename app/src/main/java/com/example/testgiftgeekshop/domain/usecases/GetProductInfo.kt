package com.example.testgiftgeekshop.domain.usecases

import android.util.Log
import com.example.geekshopappbuy.domain.entitys.GeekProductUI
import com.example.testgiftgeekshop.domain.MainRepository
import com.example.testgiftgeekshop.utils.Mapper
import com.example.testgiftgeekshop.utils.ResourceVrap
import javax.inject.Inject

class GetProductInfo @Inject constructor(
    private val repository: MainRepository
) {
    suspend fun execute(productId: Int): ResourceVrap<GeekProductUI> {
        Log.d("element", "i am in execute- GetProductInfo")
        try {
            val element = repository.getProductInfo(productId)
            if (element.isSuccessful) {
                Mapper.mapProductFormPromModelToUi(element.body.product!!).also {
                    Log.d("element", "i am in execute- element.isSuccessful")
                    Log.d("element", "element ${it.name}")
                    return ResourceVrap.Success(it)
                }
            }
            return ResourceVrap.Error(RuntimeException("element.is NOT Successful or element body = NULL"))
        } catch (e: Exception) {
            Log.d("element", "i am in catch- GetProductInfo")
            return ResourceVrap.Error(e)
        }
    }
}