package com.bignerdranch.android.safeshopping

import android.graphics.Bitmap
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.squareup.picasso.Picasso
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.Dispatcher

private const val TAG = "MapsFragmentViewModel"

class MapsFragmentViewModel : ViewModel() {
    private var defaultLat = 40.6971494
    private var defaultLong = -73.6994965
    var shopsListLiveData: LiveData<List<Shop>>
    private val shopRepository = ShopRepository.get()

    init {
        shopsListLiveData = shopRepository.fetchShops(defaultLat, defaultLong)
    }

    fun fetchShops(lat: Double, long: Double) {
        shopsListLiveData = shopRepository.fetchShops(lat, long)

    }

}