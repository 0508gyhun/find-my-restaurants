package com.example.muapp.data.source.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.muapp.data.model.RestaurantFavorite

@Dao
interface RestaurantFavoriteDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(restaurantFavorite: RestaurantFavorite)

    @Delete
    suspend fun delete(restaurantFavorite: RestaurantFavorite)

    @Query("select * from favorites where userId = :userId order by createdTime DESC")
    suspend fun getUserIdFavoriteRestaurant(userId : String) : List<RestaurantFavorite>


    @Query("SELECT * FROM favorites WHERE userId = :userId AND contentId = :contentId LIMIT 1")
    suspend fun getFavoriteIfExists(userId: String, contentId: String): RestaurantFavorite?


    @Query("DELETE  FROM favorites")
    suspend fun deleteAllFavorites()


}