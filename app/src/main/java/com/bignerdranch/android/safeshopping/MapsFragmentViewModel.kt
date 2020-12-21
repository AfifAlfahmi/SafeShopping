package com.bignerdranch.android.safeshopping

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel

class MapsFragmentViewModel : ViewModel() {

    var shopsListLiveData: LiveData<List<Shop>>
    private val fetchShops = FetchShops()

    init {
        shopsListLiveData = fetchShops.fetchShops()
    }

    fun fetchShops(){
        shopsListLiveData =fetchShops.fetchShops()

    }

}