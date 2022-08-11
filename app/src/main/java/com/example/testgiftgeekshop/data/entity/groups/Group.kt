package com.example.geekshopappbuy.data.entity.groups


import com.google.gson.annotations.SerializedName

data class Group(
    @SerializedName("description")
    val description: String?,
    @SerializedName("id")
    val id: Int?,
    @SerializedName("image")
    val image: String?,
    @SerializedName("name")
    val name: String?,
    @SerializedName("parent_group_id")
    val parentGroupId: Int?
) {
//    fun mapToUiModel(): GeekGroupUI {
//        return GeekGroupUI(
//            description = description ?: throw RuntimeException("there is no description"),
//            id = id ?: throw RuntimeException("there is no id"),
//            image = image?.replace("w200_h200","w500_h500" ) ?: throw RuntimeException("there is no image"),
//            name = name ?: throw RuntimeException("there is no name"),
//            parentGroupId = parentGroupId?:555
//        )
//    }
}