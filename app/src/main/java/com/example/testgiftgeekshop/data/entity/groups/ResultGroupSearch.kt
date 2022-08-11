package com.example.geekshopappbuy.data.entity.groups


import com.google.gson.annotations.SerializedName

data class ResultGroupSearch(
    @SerializedName("groups")
    val groups: List<Group?>?
)