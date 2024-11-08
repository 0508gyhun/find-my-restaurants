package com.example.muapp.data.source.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.muapp.data.model.RestaurantFavorite


@Database(entities = [RestaurantFavorite::class], version = 2)
abstract class AppDatabase : RoomDatabase() {

    abstract fun restaurantFavoriteDao(): RestaurantFavoriteDao

    companion object{
        fun getInstance(context : Context) :AppDatabase{
            return Room.databaseBuilder(
                context,
                AppDatabase::class.java,
                "Muapp-db"
            ).fallbackToDestructiveMigration()
                .build()
        }
    }
}