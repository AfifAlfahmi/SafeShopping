package com.bignerdranch.android.safeshopping

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.bignerdranch.android.safeshopping.weatherapi.WeatherResponse

class ShopFragmentViewModel: ViewModel() {

    var shopWeatherLiveData: LiveData<WeatherResponse>? = null
    var favoriteShopsLiveData: LiveData<List<FavoriteShop>>? = null

    private val WeatherRepo = WeatherRepository()
    private val shopRepository = ShopRepository.get()



    fun fetchShopWeather(loc:String) {
        shopWeatherLiveData = WeatherRepo.fetchWeather(loc)

    }


    fun fetchShopWeatherByDay(loc:String,day:String){
        shopWeatherLiveData = WeatherRepo.fetchWeatherByDay(loc,day)
    }

    fun fetchFavoritesShops(){
        favoriteShopsLiveData = shopRepository.getFavoritesShops()
    }

    fun deleteFavoriteShop(id:String){
        shopRepository.deleteFavoriteShop(id)
    }
}
