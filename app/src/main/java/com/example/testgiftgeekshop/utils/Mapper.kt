package com.example.testgiftgeekshop.utils

import android.util.Log
import com.example.geekshopappbuy.domain.entitys.GeekProductUI
import com.example.geekshopappbuy.data.entity.products.Product
import com.example.geekshopappbuy.data.entity.products.ResponseProductList

object Mapper {

    fun mapProductFormPromModelToUi(promModel: Product): GeekProductUI {
        val listImages = promModel.images?.map {
            it?.url ?: throw RuntimeException("there is Uri inside Image item")
        } ?: throw RuntimeException("there is empty List<Images>")

        return GeekProductUI(
            id = promModel.id ?: throw RuntimeException("there is no id"),
            name = promModel.name ?: throw RuntimeException("there is no name"),
            price = promModel.price ?: throw RuntimeException("there is no price"),
            groupId = promModel.group?.id ?: throw RuntimeException("there is no groupId"),
            group_Name = promModel.group.name ?: throw RuntimeException("there is no group_Name"),
            category = promModel.category?.caption
                ?: throw RuntimeException("there is no category"),
            description = promModel.description
                ?: throw RuntimeException("there is no description"),
            mainImage = promModel.mainImage?.replace("w200_h200", "w500_h500")
                ?: throw RuntimeException("there is no mainImage"),
            images = listImages,
            status = promModel.status?: throw RuntimeException("there is no status"),
            presence = promModel.presence?: throw RuntimeException("there is no presence"),
        )
    }

    fun mapGeekResponseProductListToUIList(netResponse: SimpleResponse<ResponseProductList>): List<GeekProductUI> {
        return when (netResponse.status) {
            is SimpleResponse.Status.Success -> {
                Log.d("TEST_TAG", "status in when Success")
                val body = netResponse.body.products
                val list = body!!.map {
                    mapProductFormPromModelToUi(it!!)
                }
                body.forEach {
                    Log.d("TEST_TAG", "result ${it!!.name}")
                    Log.d("TEST_TAG", "result ${it.price}")
                    Log.d("TEST_TAG", "result ${it.id}")
                }
                list
            }
            is SimpleResponse.Status.Failure -> {
                Log.d("TEST_TAG", "status in when Failure")
                throw RuntimeException("error inside Mapper - mapGeekResponseProductListToUIList")
            }
        }
    }
}