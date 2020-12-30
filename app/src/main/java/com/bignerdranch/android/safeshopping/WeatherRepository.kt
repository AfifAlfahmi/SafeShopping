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
private const val BASE_URL = "https://api.yelp.com/v3/"
private const val API_KEY = "vNQd7Rt3Hs63t6kdpnGU2wdzQdkyH_EO2eblSMgT-MrnguULSNpGqgWmMJadD3x_MaA_cxM7mQtSNAG01rWoOh1wS7oazCi2x9fxpGkXiTq4Q9JFm28mDaIx_ufYX3Yx"

private const val TAG = "FetchShops"

open class WeatherRepository {

    private val fetchWeatherListener = mutableListOf<WeatherResponseListener>()
    fun fetchWeather(loc:String) :LiveData<WeatherResponse>{
        val responseLiveData: MutableLiveData<WeatherResponse> = MutableLiveData()


//        val retrofit: Retrofit = Retrofit.Builder()
//                .baseUrl(BASE_WEATHER_URL)
//                .addConverterFactory(GsonConverterFactory.create()) //24.7394478,46.8098221   // 40.6971494,-73.6994965
//                .build()
        weatherApi.fetchWeather(loc)
                .enqueue(object :Callback<WeatherResponse>{

            override fun onResponse(call: Call<WeatherResponse>, response: Response<WeatherResponse>) {
                //Log.d(TAG,"onResponse weather ${response.body()?.current?.condition?.text}")
                val body = response.body()
                if(body != null){
                //    Log.d(TAG,"onResponse weather  null response body")


                    responseLiveData.value = body
                    notifyFetchWeatherListeners()
                }
            }
            override fun onFailure(call: Call<WeatherResponse>, t: Throwable) {
                Log.d(TAG,"onFailure weather $t")

            }
                })
        return responseLiveData
    }
    fun fetchWeatherTest() :LiveData<WeatherResponse>{
        val responseLiveData: MutableLiveData<WeatherResponse> = MutableLiveData()


//        val retrofit: Retrofit = Retrofit.Builder()
//                .baseUrl(BASE_WEATHER_URL)
//                .addConverterFactory(GsonConverterFactory.create()) //24.7394478,46.8098221   // 40.6971494,-73.6994965
//                .build()
        weatherApi.fetchWeather("24.7394478,46.8098221")
            .enqueue(object :Callback<WeatherResponse>{

                override fun onResponse(call: Call<WeatherResponse>, response: Response<WeatherResponse>) {
                    Log.d(TAG,"onResponse weather ${response.body()?.current?.condition?.text}")
                    val body = response.body()
                    if(body != null){
                        //    Log.d(TAG,"onResponse weather  null response body")


                        responseLiveData.value = body
                        notifyFetchWeatherListeners()
                    }
                }
                override fun onFailure(call: Call<WeatherResponse>, t: Throwable) {
                    Log.d(TAG,"onFailure weather $t")

                }
            })
        return responseLiveData
    }

    fun fetchWeatherByDay(loc:String,day:String) :LiveData<WeatherResponse>{

        val responseLiveData: MutableLiveData<WeatherResponse> = MutableLiveData()

//        val retrofit: Retrofit = Retrofit.Builder()
//            .baseUrl(BASE_WEATHER_URL)
//            .addConverterFactory(GsonConverterFactory.create()) //24.7394478,46.8098221   // 40.6971494,-73.6994965
//            .build()
        weatherApi.fetchWeatherByDay(loc,day)
            .enqueue(object :Callback<WeatherResponse>{

                override fun onResponse(call: Call<WeatherResponse>, response: Response<WeatherResponse>) {
                    Log.d(TAG,"onResponse weather by day ${response.body()?.forecast?.forecastday?.get(0)?.date}")
                    val body = response.body()
                    if(body != null){
                        //    Log.d(TAG,"onResponse weather  null response body")
                        notifyFetchWeatherListeners()
                        responseLiveData.value = body
                    }
                }
                override fun onFailure(call: Call<WeatherResponse>, t: Throwable) {
                    Log.d(TAG,"onFailure weather by day $t")

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
            val client = OkHttpClient.Builder()
                .build()
            val retrofit: Retrofit   = Retrofit
                .Builder()
                .baseUrl(BASE_WEATHER_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            weatherRepository = WeatherRepository()
            weatherApi = retrofit.create(WeatherApi::class.java)
        }


    }

}