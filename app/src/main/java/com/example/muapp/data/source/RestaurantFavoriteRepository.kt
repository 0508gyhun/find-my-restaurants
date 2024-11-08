package com.example.muapp.data.source

import com.example.muapp.ApiConstants.ApiConstants
import com.example.muapp.data.model.RestaurantEntity
import com.example.muapp.data.model.RestaurantFavorite
import com.example.muapp.data.source.local.RestaurantFavoriteDao
import javax.inject.Inject

class RestaurantFavoriteRepository @Inject constructor(
    private val dao : RestaurantFavoriteDao,
    private val restaurantService: RestaurantService) {

    suspend fun getAllFavoriteRestaurant(userId:String): List<RestaurantFavorite>
    {
        return dao.getUserIdFavoriteRestaurant(userId)
    }

    suspend fun getRestaurantAreaRecommend(areaCode : String, sigunguCode: String) : RestaurantEntity
    {
        return restaurantService.getRestaurantAreaRecommend(ApiConstants.SERVICE_KEY_DECODE,areaCode,sigunguCode)
    }

}