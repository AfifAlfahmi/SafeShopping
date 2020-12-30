package com.bignerdranch.android.safeshopping

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.bignerdranch.android.safeshopping.weatherapi.WeatherResponse

class ShopFragmentViewModel: ViewModel() {

    var shopWeatherLiveData: LiveData<WeatherResponse>? = null
    private val WeatherRepo = WeatherRepository()

    init {
        //shopLiveData = fetchShops.fetchShopDetail()
    }

    fun fetchShopWeather(loc:String) {
        shopWeatherLiveData = WeatherRepo.fetchWeather(loc)

    }

    fun fetchShopWeatherByDay(loc:String,day:String){
        shopWeatherLiveData = WeatherRepo.fetchWeatherByDay(loc,day)
    }
}
