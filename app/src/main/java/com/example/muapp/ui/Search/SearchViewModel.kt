package com.example.muapp.ui.Search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.muapp.data.model.RestaurantItem
import com.example.muapp.data.model.RestaurantSearchEntity
import com.example.muapp.data.source.SearchRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(private val repository: SearchRepository) :ViewModel() {

    private val _searchResults = MutableLiveData<List<RestaurantItem>>(emptyList())
    val searchResults :LiveData<List<RestaurantItem>> = _searchResults

    private var currentItems = mutableListOf<RestaurantItem>()
    private var currentPosition = 0
    private val initialLoadSize = 7
    private val pageSize = 3


    fun searchRestaurants(newText : String?)
    {
        if(newText.isNullOrEmpty())
        {
            currentItems.clear()
            currentPosition = 0
            _searchResults.value = currentItems
            return
        }

        viewModelScope.launch {
            val restaurantSearchEntity =
                repository.getRestaurantSearchKeywordResponse(newText)
            if(restaurantSearchEntity.response.body.restaurantItems !is String)
            {
                val restaurantValidSearchEntity =
                    repository.getRestaurantSearchValidResponse(newText)
                val allItems =
                    restaurantValidSearchEntity.response.body.restaurantItems.restaurantItem
                currentItems = allItems.take(initialLoadSize).toMutableList()
                currentPosition = initialLoadSize
                _searchResults.value = currentItems.toList()
            }

        }
    }
    fun loadMore(){

        viewModelScope.launch {
            val restaurantEntity = repository.getRestaurantEntity()
            val allItems = restaurantEntity.response.body.restaurantItems.restaurantItem

            val nextItems = allItems.drop(currentPosition).take(pageSize)
            if (nextItems.isNotEmpty())
            {
                currentItems.addAll(nextItems)
                currentPosition += pageSize
                _searchResults.value = currentItems.toList()
            }
        }
    }
    companion object{
        fun provideFactory(repository: SearchRepository) = viewModelFactory {
            initializer {
                SearchViewModel(repository)
            }
        }
    }
}