package com.geekshop.testgiftgeekshop.utils

import android.util.Log
import com.geekshop.geekshopappbuy.data.entity.products.Image
import com.geekshop.geekshopappbuy.domain.entitys.GeekProductUI
import com.geekshop.geekshopappbuy.data.entity.products.Product
import com.geekshop.geekshopappbuy.data.entity.products.ResponseProductList
import com.geekshop.testgiftgeekshop.domain.ImageUI

object Mapper {

    fun mapProductFormPromModelToUi(promModel: Product): GeekProductUI {
//        val listImages = promModel.images?.map {
//            it?.url ?: throw RuntimeException("there is Uri inside Image item")
//        } ?: throw RuntimeException("there is empty List<Images>")

//        val listImagesTest = promModel.images?.map {
//            it?: throw RuntimeException("there is Uri inside Image item")
//        } ?: throw RuntimeException("there is empty List<Images>")


        //this 2 fun
        fun convertToUiImage(image: Image?): ImageUI {
            return ImageUI(
                image?.id ?:throw RuntimeException("id or image is empty "),
                image.url?.replace("w200_h200", "w700_h700")
                    ?: throw RuntimeException("url is empty ")
            )
        }
        fun convertTestTest(): List<ImageUI> {
            val tempList = mutableListOf<ImageUI>()
            for (image in promModel.images
                ?: throw RuntimeException("there is empty List<Images>")) {
                val res = convertToUiImage(image)
                tempList.add(res)
            }
            Log.d("image_test", "tempList ${tempList}")
            return tempList
        }

        fun convertImagesToNormal(): List<Image> {
            Log.d("image_test", "i am in Mapper")
            Log.d("image_test", "list images ${promModel.images}")
            val temp = mutableListOf<Image>()
            for (image in promModel.images
                ?: throw RuntimeException("there is empty List<Images>")) {
                val tempImage = image?.copy() ?: throw RuntimeException("image is empty ")
                tempImage.url?.replace("w100_h100", "w700_h700")
                tempImage.thumbnailUrl?.replace("w100_h100", "w700_h700")
                tempImage.thumbnailUrl?.replace("w100_h100", "w700_h700")
                temp.add(tempImage)
            }
            Log.d("image_test", "list images ${temp}")
            return temp

//            val tempList = mutableListOf<ImageUI>()
//            for (image in promModel.images
//                ?: throw RuntimeException("there is empty List<Images>")) {
//                val res = convertToUiImage(image)
//                tempList.add(res)
//            }
//            return tempList
        }


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
            images = convertTestTest(),
            status = promModel.status ?: throw RuntimeException("there is no status"),
            presence = promModel.presence ?: throw RuntimeException("there is no presence"),
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