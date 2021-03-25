package com.bignerdranch.android.safeshopping

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ShopsListViewModel : ViewModel() {
    var lat: Double? = null
    var long: Double? = null
    lateinit var shopsListLiveData: LiveData<List<Shop>>
    private val shopRepository = ShopRepository.get()
    var rotated: Boolean = false
    lateinit var shopsLocalListLiveData: LiveData<List<Shop>>
    lateinit var searchListLiveData: LiveData<List<Search>>

    fun fetchShops(lat: Double?, long: Double?) {
        shopsListLiveData = shopRepository.fetchShops(lat, long)

    }

    fun searchShops(lat: Double?, long: Double?, searchTerm: String) {
        shopsListLiveData = shopRepository.searchShops(lat, long, searchTerm)

    }

    fun addShops(shopsList: List<Shop>) {
        shopRepository.addShops(shopsList)
    }

    fun deleteShops() {
        shopRepository.deleteShops()
    }

    fun getShopsFromRoom() {

        shopsLocalListLiveData = shopRepository.getShops()
    }

    fun addSearchTerm(searchTerm: Search) {
        shopRepository.addSearchTerm(searchTerm)

    }

    fun getSearchTerms() {
        searchListLiveData = shopRepository.getSearchTerms()
    }

    fun deleteSearchTerms() {
        shopRepository.deleteSearchTerms()
    }

}