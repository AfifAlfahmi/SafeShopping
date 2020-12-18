package com.bignerdranch.android.safeshopping

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.bignerdranch.android.safeshopping.weatherapi.WeatherResponse

class ShopFragmentViewModel: ViewModel() {

    var shopLiveData: LiveData<Shop>? = null
    var shopWeatherLiveData: LiveData<WeatherResponse>? = null
    private val fetchShops = FetchShops()

    init {
        //shopLiveData = fetchShops.fetchShopDetail()
    }

    fun fetchShopWeather(loc:String) {
        shopWeatherLiveData = fetchShops.fetchWeather(loc)

    }
}
