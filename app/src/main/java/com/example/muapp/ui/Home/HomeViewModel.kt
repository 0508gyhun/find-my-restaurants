package com.example.muapp.ui.Home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.muapp.data.model.RestaurantAccessInfoItem
import com.example.muapp.data.model.RestaurantFavorite
import com.example.muapp.data.model.RestaurantInfoItem
import com.example.muapp.data.model.RestaurantItem
import com.example.muapp.data.source.RestaurantFavoriteRepository
import com.example.muapp.data.source.RestaurantRepository
import com.example.muapp.ui.MyPage.FavoriteViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject
@HiltViewModel
class HomeViewModel @Inject constructor(private val repository: RestaurantRepository):ViewModel() {


    private val _restaurantFavorite = MutableLiveData<RestaurantFavorite>()
    val restaurantFavorite: LiveData<RestaurantFavorite> = _restaurantFavorite

    private val _isFavorite = MutableLiveData<Boolean>()
    val isFavorite: LiveData<Boolean> = _isFavorite

    private val _restaurantList = MutableLiveData<List<RestaurantItem>>()
    val restaurantList: LiveData<List<RestaurantItem>> = _restaurantList

    // RestaurantInfoItem으로 타입 수정
    private val _restaurantInfo = MutableLiveData<RestaurantInfoItem>()
    val restaurantInfo: LiveData<RestaurantInfoItem> = _restaurantInfo

    // RestaurantAccessInfoItem으로 타입 수정
    private val _restaurantAccessInfo = MutableLiveData<RestaurantAccessInfoItem>()
    val restaurantAccessInfo: LiveData<RestaurantAccessInfoItem> = _restaurantAccessInfo


    fun loadInitialRestaurant(){
        viewModelScope.launch {
            val response = repository.getRestaurantEntity()
            val itemList = response.response.body.restaurantItems.restaurantItem
            _restaurantList.value = itemList
        }
    }

    fun loadRestaurantInfo(contentId : String){
        viewModelScope.launch {
            val response = repository.getRestaurantInfoEntity(contentId)
            _restaurantInfo.value = response.response.body.restaurantItems.restaurantItem.first()

        }
    }

    fun loadRestaurantAccessInfo(contentId: String)
    {
        viewModelScope.launch {
            val response = repository.getRestaurantAccessInfoEntity(contentId)
            _restaurantAccessInfo.value = response.response.body.restaurantItems.restaurantItem.first()
        }
    }
    fun readyForFavoriteRestaurant(item: RestaurantItem, uid : String)
    {
        _restaurantFavorite.value = RestaurantFavorite(
            userId = uid,
            contentId = item.contentId,
            contentTitle = item.title,
            contentAddress = item.address,
            imageUrl = item.imageUrl1,
            areaCode = item.areaCode,
            sigunguCode = item.sigunguCode
        )
    }
    fun checkFavoriteStatus(contentId: String, uid :String)
    {
        viewModelScope.launch {
            val existingFavorite = repository.getFavoriteIfExists(
                uid,
                contentId
            )
            _isFavorite.value = existingFavorite != null
        }
    }

    fun toggleFavorite(uid: String) {
        viewModelScope.launch {
            val currentFavorite = _restaurantFavorite.value ?: return@launch
            val existingFavorite = repository.getFavoriteIfExists(
                uid,
                currentFavorite.contentId
            )

            if (existingFavorite == null) {
                repository.insertFavorite(currentFavorite)
                _isFavorite.value = true
            } else {
                repository.deleteFavorite(existingFavorite)
                _isFavorite.value = false
            }
        }
    }
//
//    companion object{
//        fun provideFactory(repository: RestaurantRepository) = viewModelFactory {
//            initializer {
//                HomeViewModel(repository)
//            }
//        }
//    }

}