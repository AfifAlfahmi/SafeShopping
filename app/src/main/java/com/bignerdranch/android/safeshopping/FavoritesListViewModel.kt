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

    fun addFavoriteShop(favshop:FavoriteShop) {
        shopRepository.addFavoriteShop(favshop)
    }

    fun getFavoritesShops(){

        favoritesListLiveData = shopRepository.getFavoritesShops()
    }



}