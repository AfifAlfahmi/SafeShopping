package com.bignerdranch.android.safeshopping

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ShopsListViewModel : ViewModel() {
      var lat: Double? = null
    var long: Double? = null
    var shopsListLiveData: LiveData<List<Shop>>
    private val shopRepository = ShopRepository.get()
    lateinit var shopsLocalListLiveData :LiveData<List<Shop>>
    lateinit var searchListLiveData :LiveData<List<Search>>

    private val fetchShops = FetchShops()

    init {
        shopsListLiveData = fetchShops.fetchShops()
    }

    fun fetchShops(){
        shopsListLiveData =fetchShops.fetchShops()

    }
    fun searchShops(searchTerm:String){
        shopsListLiveData =fetchShops.searchShops(searchTerm)

    }
  fun addShops(shopsList:List<Shop>) {
      shopRepository.addShops(shopsList)
  }

    fun getShopsFromRoom(){

        shopsLocalListLiveData = shopRepository.getShops()
    }


    fun addSearchTerm(searchTerm: Search){
         shopRepository.addSearchTerm(searchTerm)

    }
    fun getSearchTerms(){
        searchListLiveData = shopRepository.getSearchTerms()
    }
    fun deleteSearchTerms(){
        shopRepository.deleteSearchTerms()
    }


}