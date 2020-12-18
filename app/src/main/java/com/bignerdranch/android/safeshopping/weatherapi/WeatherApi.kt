package com.bignerdranch.android.safeshopping.weatherapi

import com.bignerdranch.android.safeshopping.yelpapi.YelpResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query

interface WeatherApi {


    @GET("forecast.json?key=fa5111ad1ee942d3bd1170025201512&days=2")
    fun fetchWeather(
        @Query("q")loc:String
    ): Call<WeatherResponse>
}