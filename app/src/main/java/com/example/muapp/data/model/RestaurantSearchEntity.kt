package com.example.muapp.data.model

import com.google.gson.annotations.SerializedName

data class RestaurantSearchEntity(
    @SerializedName("response")
    val response : RestaurantSearchResponse
)

data class RestaurantSearchResponse(
    @SerializedName("header")
    val header: RestaurantSearchHeader,

    @SerializedName("body")
    val body : RestaurantSearchBody
)
data class RestaurantSearchHeader(
    @SerializedName("resultCode")
    val resultCode : String,

    @SerializedName("resultMsg")
    val resultMsg : String
)
data class RestaurantSearchBody(
    @SerializedName("items")
    val restaurantItems : Any,
    @SerializedName("totalCount")
    val totalCount :Int
)
