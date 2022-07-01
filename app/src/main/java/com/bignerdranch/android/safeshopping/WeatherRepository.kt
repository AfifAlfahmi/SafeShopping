package com.bignerdranch.android.safeshopping

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.bignerdranch.android.safeshopping.listener.WeatherResponseListener
import com.bignerdranch.android.safeshopping.weatherapi.Condition
import com.bignerdranch.android.safeshopping.weatherapi.CurrentWeather
import com.bignerdranch.android.safeshopping.weatherapi.WeatherApi
import com.bignerdranch.android.safeshopping.weatherapi.WeatherResponse
import com.bignerdranch.android.safeshopping.yelpapi.YelpApi
import com.bignerdranch.android.safeshopping.yelpapi.YelpResponse
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

private const val BASE_WEATHER_URL = "https://api.weatherapi.com/v1/"
private const val TAG = "FetchShops"

open class WeatherRepository {

    private val fetchWeatherListener =
        mutableListOf<WeatherResponseListener>()

    fun fetchWeather(loc: String): LiveData<WeatherResponse> {

        val responseLiveData: MutableLiveData<WeatherResponse> = MutableLiveData()
        weatherApi.fetchWeather(loc)
            .enqueue(object : Callback<WeatherResponse> {
                override fun onResponse(
                    call: Call<WeatherResponse>,
                    response: Response<WeatherResponse>
                ) {
                    val body = response.body()
                    if (body != null) {
                        responseLiveData.value = body
                        notifyFetchWeatherListeners()
                    }
                }
                override fun onFailure(call: Call<WeatherResponse>, t: Throwable) {

                }
            })
        return responseLiveData
    }

    fun fetchWeatherByDay(loc: String, day: String): LiveData<WeatherResponse> {

        val responseLiveData: MutableLiveData<WeatherResponse> = MutableLiveData()
        weatherApi.fetchWeatherByDay(loc, day)
            .enqueue(object : Callback<WeatherResponse> {

                override fun onResponse(
                    call: Call<WeatherResponse>,
                    response: Response<WeatherResponse>
                ) {
                    val body = response.body()
                    if (body != null) {
                        notifyFetchWeatherListeners()
                        responseLiveData.value = body
                    }
                }

                override fun onFailure(call: Call<WeatherResponse>, t: Throwable) {

                }
            })
        return responseLiveData
    }

    fun addFetchWeatherListener(listener: WeatherResponseListener) {
        fetchWeatherListener += listener
    }

    fun removeFetchWeatherListener(listener: WeatherResponseListener) {
        fetchWeatherListener -= listener
    }

    private fun notifyFetchWeatherListeners() {
        for (listener in fetchWeatherListener) {
            listener.onFetchWeatherSuccess()
        }
    }

    companion object {
        var weatherRepository: WeatherRepository? = null
        private var weatherApi: WeatherApi

        init {
            val retrofit: Retrofit = Retrofit
                .Builder()
                .baseUrl(BASE_WEATHER_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
            weatherRepository = WeatherRepository()
            weatherApi = retrofit.create(WeatherApi::class.java)
        }
    }
}