package com.example.muapp.data.model

import com.google.gson.annotations.SerializedName
import okhttp3.internal.http2.Header

data class RestaurantEntity(
    @SerializedName("response")
    val response : RestaurantResponse
)

data class RestaurantResponse(
    @SerializedName("header")
    val header: RestaurantHeader,

    @SerializedName("body")
    val body : RestaurantBody
)
data class RestaurantHeader(
    @SerializedName("resultCode")
    val resultCode : String,

    @SerializedName("resultMsg")
    val resultMsg : String
)
data class RestaurantBody(
    @SerializedName("items")
    val restaurantItems : RestaurantItems
)
data class RestaurantItems(
    @SerializedName("item")
    val restaurantItem : List<RestaurantItem>
)
data class RestaurantItem(
    @SerializedName("addr1")
    var address : String,

    @SerializedName("areacode")
    val areaCode : String,
    @SerializedName("sigungucode")
    val sigunguCode : String,
    @SerializedName("firstimage")
    var imageUrl1 : String,
    @SerializedName("firstimage2")
    var imageUrl2: String,

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
    var tel : String,
    @SerializedName("title")
    var title : String
)



