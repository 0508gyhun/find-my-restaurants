package com.example.muapp.data.source


import com.example.muapp.data.model.RestaurantAccessInfoEntity
import com.example.muapp.data.model.RestaurantEntity
import com.example.muapp.data.model.RestaurantInfoEntity
import com.example.muapp.data.model.RestaurantSearchEntity
import com.example.muapp.data.model.RestaurantValidSearchEntity
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface RestaurantService {
    //serviceKey=
    // dNN2pYJfuK8SHtk%2Baw0brnS92FG8ynVaAqvQmtbSkYVp4woSoaYAqMjJtpT6VJA7Rmn2b9P9u44h%2BbzCXa%2BVqw%3D%3D

    @GET("B551011/KorWithService1/areaBasedList1?numOfRows=1000000&pageNo=1&MobileOS=ETC&MobileApp=AppTest&_type=json&contentTypeId=39")
    suspend fun getAllRestaurant(
        @Query("serviceKey") serviceKey : String) : RestaurantEntity


    @GET("B551011/KorWithService1/detailIntro1?numOfRows=10&pageNo=1&MobileOS=ETC&MobileApp=AppTest&contentTypeId=39&_type=json")
    suspend fun getRestaurantInfo(
        @Query("serviceKey") serviceKey: String,
        @Query("contentId") contentId : String
    ) :RestaurantInfoEntity


    @GET("B551011/KorWithService1/detailWithTour1?numOfRows=100000&pageNo=1&MobileOS=ETC&MobileApp=AppTest&_type=json")
    suspend fun getRestaurantAccessInfo(
        @Query("serviceKey") serviceKey: String,
        @Query("contentId") contentId: String
    ) : RestaurantAccessInfoEntity

    @GET("/B551011/KorWithService1/searchKeyword1?numOfRows=10&pageNo=1&MobileOS=ETC&MobileApp=AppTest&_type=json&contentTypeId=39")
    suspend fun getRestaurantSearchKeywordResponse(
        @Query("serviceKey") serviceKey: String,
        @Query("keyword") keyword: String
    ) : RestaurantSearchEntity

    @GET("/B551011/KorWithService1/searchKeyword1?numOfRows=10&pageNo=1&MobileOS=ETC&MobileApp=AppTest&_type=json&contentTypeId=39")
    suspend fun getRestaurantSearchValidSearch(
        @Query("serviceKey") serviceKey: String,
        @Query("keyword") keyword: String
    ) : RestaurantEntity
    //areaCode=33&sigunguCode=7
    @GET("/B551011/KorWithService1/areaBasedList1?numOfRows=5&pageNo=1&MobileOS=ETC&MobileApp=AppTest&_type=json&contentTypeId=39")
    suspend fun getRestaurantAreaRecommend(
        @Query("serviceKey") serviceKey: String,
        @Query("areaCode") areaCode : String,
        @Query("sigunguCode") sigunguCode : String
    ): RestaurantEntity

    companion object{
        fun create(): RestaurantService{
            val logger = HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            }
            val client = OkHttpClient.Builder()
                .addInterceptor(logger)
                .build()
//            val gson = GsonBuilder()
//                .setLenient()  // 덜 엄격한 JSON 파싱 허용
//                .create()

            return Retrofit.Builder()
                .baseUrl("https://apis.data.go.kr/")
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(RestaurantService::class.java)
        }
    }
}