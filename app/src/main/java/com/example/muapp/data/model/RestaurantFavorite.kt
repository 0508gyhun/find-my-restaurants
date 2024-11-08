package com.example.muapp.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "favorites")
data class RestaurantFavorite(
    @PrimaryKey (autoGenerate = true)
    var id :Long = 0,
    var userId : String,
    var contentId : String,
    var contentTitle : String,
    var contentAddress: String,
    var imageUrl: String,
    var areaCode: String,
    var sigunguCode : String,
    var createdTime : Long = System.currentTimeMillis()
)
