package com.example.muapp.data.model

import com.google.gson.annotations.SerializedName

data class RestaurantAccessInfoEntity(
    @SerializedName("response")
    val response : RestaurantAccessInfoResponse
)

data class RestaurantAccessInfoResponse(
    @SerializedName("header")
    val header: RestaurantAccessInfoHeader,

    @SerializedName("body")
    val body : RestaurantAccessInfoBody
)
data class RestaurantAccessInfoHeader(
    @SerializedName("resultCode")
    val resultCode : String,

    @SerializedName("resultMsg")
    val resultMsg : String
)
data class RestaurantAccessInfoBody(
    @SerializedName("items")
    val restaurantItems : RestaurantAccessInfoItems
)
data class RestaurantAccessInfoItems(
    @SerializedName("item")
    val restaurantItem : List<RestaurantAccessInfoItem>
)
data class RestaurantAccessInfoItem(
    @SerializedName("parking")
    val parking : String,
    @SerializedName("route")
    val route : String,
    @SerializedName("publictransport")
    val accessInfo : String,
    @SerializedName("wheelchair")
    val wheelchairInfo: String,

    @SerializedName("exit")
    val exit : String,
    @SerializedName("elevator")
    val elevator : String,

    @SerializedName("handicapetc")
    val etcInfo : String
)
