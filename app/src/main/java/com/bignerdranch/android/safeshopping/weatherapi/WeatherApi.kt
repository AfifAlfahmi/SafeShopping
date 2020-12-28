package com.bignerdranch.android.safeshopping.weatherapi

import com.bignerdranch.android.safeshopping.yelpapi.YelpResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query

interface WeatherApi {


    @GET("forecast.json?key=26cfcf64e8634ecb84891320202712&days=1")
    fun fetchWeather(
        @Query("q")loc:String
    ): Call<WeatherResponse>

    @GET("forecast.json?key=26cfcf64e8634ecb84891320202712&days=1")
    fun fetchWeatherByDay( @Query("q")loc:String,@Query("dt")day:String): Call<WeatherResponse>

}