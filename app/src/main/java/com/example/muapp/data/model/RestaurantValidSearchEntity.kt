package com.example.muapp.data.model

import com.google.gson.annotations.SerializedName

data class RestaurantValidSearchEntity(
    @SerializedName("response")
    val response : RestaurantValidResponse
)

data class RestaurantValidResponse(
    @SerializedName("header")
    val header: RestaurantValidHeader,

    @SerializedName("body")
    val body : RestaurantValidBody
)
data class RestaurantValidHeader(
    @SerializedName("resultCode")
    val resultCode : String,

    @SerializedName("resultMsg")
    val resultMsg : String
)
data class RestaurantValidBody(
    @SerializedName("items")
    val restaurantItems : RestaurantValidItems
)
data class RestaurantValidItems(
    @SerializedName("item")
    val restaurantItem : List<RestaurantValidItem>
)
data class RestaurantValidItem(
    @SerializedName("addr1")
    val address : String?,

    @SerializedName("areacode")
    val areaCode : String?,
    @SerializedName("firstimage")
    val imageUrl1 : String,
    @SerializedName("firstimage2")
    val imageUrl2: String,

    @SerializedName("contentid")
    val contentId : String,
    @SerializedName("contenttypeid")
    val contentTypeId : String,

    @SerializedName("mapx")
    val mapX : String,
    @SerializedName("mapy")
    val mapY : String,
    @SerializedName("modifiedtime")
    val modifiedTime : String,
    @SerializedName("tel")
    val tel : String,
    @SerializedName("title")
    val title : String
)

