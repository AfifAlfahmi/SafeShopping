package com.bignerdranch.android.safeshopping.weatherapi

import com.bignerdranch.android.safeshopping.yelpapi.YelpResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface WeatherApi {


    @GET("forecast.json?key=fa5111ad1ee942d3bd1170025201512&q=24.7394478,46.8098221&days=1")
    fun fetchWeather(): Call<WeatherResponse>
}