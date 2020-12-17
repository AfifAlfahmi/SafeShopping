package com.bignerdranch.android.safeshopping

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ShopsListViewModel : ViewModel() {
      var lat: Double? = null
    var long: Double? = null
    var shopsListLiveData: LiveData<List<Shop>>
    private val fetchShops = FetchShops()

    init {
        shopsListLiveData = fetchShops.fetchShops()
    }

    fun fetchShops(){
        shopsListLiveData =fetchShops.fetchShops()

    }



}