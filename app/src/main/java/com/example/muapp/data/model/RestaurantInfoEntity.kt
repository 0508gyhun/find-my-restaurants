package com.example.muapp.data.model

import com.google.gson.annotations.SerializedName


data class RestaurantInfoEntity(
    @SerializedName("response")
    val response : RestaurantInfoResponse
)

data class RestaurantInfoResponse(
    @SerializedName("header")
    val header: RestaurantInfoHeader,

    @SerializedName("body")
    val body : RestaurantInfoBody
)
data class RestaurantInfoHeader(
    @SerializedName("resultCode")
    val resultCode : String,

    @SerializedName("resultMsg")
    val resultMsg : String
)
data class RestaurantInfoBody(
    @SerializedName("items")
    val restaurantItems : RestaurantInfoItems
)
data class RestaurantInfoItems(
    @SerializedName("item")
    val restaurantItem : List<RestaurantInfoItem>
)
data class RestaurantInfoItem(
    @SerializedName("seat")
    val seat : String,

    @SerializedName("firstmenu")
    val firstMenu : String,
    @SerializedName("treatmenu")
    val treatMenu : String,
    @SerializedName("smoking")
    val smoking: String,

    @SerializedName("packing")
    val packing : String,
    @SerializedName("parkingfood")
    val parking : String,

    @SerializedName("opentimefood")
    val openTime : String,
    @SerializedName("restdatefood")
    val restDate : String,
    @SerializedName("reservationfood")
    val reservationNumber : String
)