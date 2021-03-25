package com.bignerdranch.android.safeshopping

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class FavoritesListViewModel : ViewModel() {

    var favoritesListLiveData: LiveData<List<FavoriteShop>>
    private val shopRepository = ShopRepository.get()

    init {
        favoritesListLiveData = shopRepository.getFavoritesShops()
    }

    fun deleteFavoriteShop(id: String) {
        shopRepository.deleteFavoriteShop(id)
    }

    fun getFavoritesShops() {

        favoritesListLiveData = shopRepository.getFavoritesShops()
    }

}