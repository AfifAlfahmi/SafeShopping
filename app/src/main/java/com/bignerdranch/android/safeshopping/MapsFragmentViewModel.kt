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

    var shopsListLiveData: LiveData<List<Shop>>
    private val shopRepository = ShopRepository.get()


    init {
        shopsListLiveData = shopRepository.fetchShops()
    }


    fun fetchShops(){
        shopsListLiveData =shopRepository.fetchShops()

    }

}