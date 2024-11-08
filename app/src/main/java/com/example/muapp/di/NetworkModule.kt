package com.example.muapp.di

import com.example.muapp.data.source.RestaurantService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideOkhttp():OkHttpClient{
        val logger = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
        return OkHttpClient.Builder()
            .addInterceptor(logger)
            .build()
    }



    @Provides
    @Singleton
    fun provideRetrofit(client: OkHttpClient): Retrofit
    {
        return Retrofit.Builder()
            .baseUrl("https://apis.data.go.kr/")
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
    @Provides
    @Singleton
    fun provideRestaurantService(retrofit: Retrofit) : RestaurantService
    {
        return retrofit.create(RestaurantService::class.java)
    }


}