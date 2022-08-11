package com.example.testgiftgeekshop.data

import com.example.geekshopappbuy.data.entity.groups.ResultGroupSearch
import com.example.geekshopappbuy.data.entity.products.ResponseProductList
import com.example.geekshopappbuy.data.entity.singleproduct.ResponseSingleProduct
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface PromAPI {


    @GET("products/list")
    suspend fun getProductsByGroupID(
        @Query("group_id") group_id: Int =98631438,
        @Query("limit") limit: Int =100
    ): Response<ResponseProductList>

    @GET("products/{id}")
    suspend fun getProductInfo(
        @Path("id") id: Int,
    ): Response<ResponseSingleProduct>

    @GET("groups/list")
    suspend fun getAllGroups(
        @Query("limit") limit: Int =100
    ): Response<ResultGroupSearch>
}