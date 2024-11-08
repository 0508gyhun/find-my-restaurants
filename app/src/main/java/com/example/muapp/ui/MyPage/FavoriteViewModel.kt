package com.example.muapp.ui.MyPage

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.muapp.data.model.RestaurantFavorite
import com.example.muapp.data.model.RestaurantItem
import com.example.muapp.data.source.RestaurantFavoriteRepository
import com.example.muapp.data.source.SearchRepository
import com.example.muapp.data.source.local.RestaurantFavoriteDao
import com.example.muapp.ui.Search.SearchViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoriteViewModel @Inject constructor(private val repository: RestaurantFavoriteRepository) : ViewModel() {

    private val _favoriteItems =  MutableLiveData<List<RestaurantFavorite>>()
    val favoriteItems : LiveData<List<RestaurantFavorite>> = _favoriteItems


    private val _recommendItems = MutableLiveData<List<List<RestaurantItem>>>()
    val recommendItems : LiveData<List<List<RestaurantItem>>> = _recommendItems

    fun loadDataFromDatabase(userUid : String)
    {
        viewModelScope.launch {
            val itemList = repository.getAllFavoriteRestaurant(userUid)
            _favoriteItems.value = itemList

            // secondList 초기화 및 데이터 로딩
            val secondList: MutableList<List<RestaurantItem>> = mutableListOf()
            itemList.forEach {
                val restaurantEntity = repository.getRestaurantAreaRecommend(it.areaCode, it.sigunguCode)
                val subItemList = restaurantEntity.response.body.restaurantItems.restaurantItem
                secondList.add(subItemList)
            }
            _recommendItems.value = secondList
        }

    }
    fun getRecommendationsForPosition(position: Int): List<RestaurantItem>? {
        return recommendItems.value?.getOrNull(position)
    }

    companion object{
        fun provideFactory(repository: RestaurantFavoriteRepository) = viewModelFactory {
            initializer {
                FavoriteViewModel(repository)
            }
        }
    }

}