package com.example.muapp.data.source

import androidx.room.Query
import com.example.muapp.ApiConstants.ApiConstants
import com.example.muapp.data.model.RestaurantAccessInfoEntity
import com.example.muapp.data.model.RestaurantEntity
import com.example.muapp.data.model.RestaurantFavorite
import com.example.muapp.data.model.RestaurantInfoEntity
import com.example.muapp.data.source.local.RestaurantFavoriteDao
import retrofit2.Call
import retrofit2.Response
import javax.inject.Inject

class RestaurantRepository @Inject constructor(
    private val restaurantService : RestaurantService,
    private val dao : RestaurantFavoriteDao
){
    suspend fun getRestaurantEntity() : RestaurantEntity  {
        return restaurantService.getAllRestaurant(ApiConstants.SERVICE_KEY_DECODE)
    }
    suspend fun getRestaurantInfoEntity(contentId : String): RestaurantInfoEntity{
        return restaurantService.getRestaurantInfo(ApiConstants.SERVICE_KEY_DECODE, contentId)
    }
    suspend fun getRestaurantAccessInfoEntity(contentId: String): RestaurantAccessInfoEntity{
        return restaurantService.getRestaurantAccessInfo(ApiConstants.SERVICE_KEY_DECODE, contentId)
    }
    suspend fun getFavoriteIfExists(userId: String, contentId: String) : RestaurantFavorite?{
        return dao.getFavoriteIfExists(userId,contentId)
    }
    suspend fun insertFavorite(restaurantFavorite: RestaurantFavorite){
        return dao.insert(restaurantFavorite)
    }
    suspend fun deleteFavorite(restaurantFavorite: RestaurantFavorite){
        return dao.delete(restaurantFavorite)
    }

//
//    @Query("SELECT * FROM favorites WHERE userId = :userId AND contentId = :contentId LIMIT 1")
//    suspend fun getFavoriteIfExists(userId: String, contentId: String): RestaurantFavorite?


}
