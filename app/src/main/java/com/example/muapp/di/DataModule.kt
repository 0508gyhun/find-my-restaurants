package com.example.muapp.di

import android.content.Context
import androidx.room.Room
import com.example.muapp.data.source.local.AppDatabase
import com.example.muapp.data.source.local.RestaurantFavoriteDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object DataModule {

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context) : AppDatabase
    {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "Muapp-db"
        ).fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    @Singleton
    fun provideRestaurantFavoriteDao(appDatabase: AppDatabase) : RestaurantFavoriteDao{
        return appDatabase.restaurantFavoriteDao()
    }
}