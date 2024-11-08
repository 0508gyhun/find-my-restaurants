package com.example.muapp.data.source


import com.example.muapp.ApiConstants.ApiConstants
import com.example.muapp.data.model.RestaurantEntity
import com.example.muapp.data.model.RestaurantSearchEntity
import javax.inject.Inject


class SearchRepository @Inject constructor(
     private val restaurantService : RestaurantService
) {
    suspend fun getRestaurantEntity(): RestaurantEntity {
        return restaurantService.getAllRestaurant(ApiConstants.SERVICE_KEY_DECODE)
    }
    suspend fun getRestaurantSearchKeywordResponse(keyword :String) : RestaurantSearchEntity
    {
        return restaurantService.getRestaurantSearchKeywordResponse(ApiConstants.SERVICE_KEY_DECODE,keyword)
    }
    suspend fun getRestaurantSearchValidResponse(keyword: String) :RestaurantEntity
    {
        return restaurantService.getRestaurantSearchValidSearch(ApiConstants.SERVICE_KEY_DECODE, keyword)
    }


}
//serviceKey=dNN2pYJfuK8SHtk%2Baw0brnS92FG8ynVaAqvQmtbSkYVp4woSoaYAqMjJtpT6VJA7Rmn2b9P9u44h%2BbzCXa%2BVqw%3D%3D
//https://apis.data.go.kr/B551011/KorWithService1/searchKeyword1?&numOfRows=10&pageNo=1&MobileOS=ETC&MobileApp=AppTest&listYN=Y&arrange=A&_type=json&contentTypeId=39
//잘낭ㅎㅁ
//&keyword=%EA%B9%80%EB%B0%A5
//serviceKey=dNN2pYJfuK8SHtk%2Baw0brnS92FG8ynVaAqvQmtbSkYVp4woSoaYAqMjJtpT6VJA7Rmn2b9P9u44h%2BbzCXa%2BVqw%3D%3D&
// keyword=%25EA%25B9%2580%25EB%25B0%25A5
//https://apis.data.go.kr/B551011/KorWithService1/searchKeyword1?numOfRows=10&pageNo=1&MobileOS=ETC&MobileApp=AppTest&_type=json&contentTypeId=39&