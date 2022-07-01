package com.bignerdranch.android.safeshopping.weatherapi

import com.google.gson.annotations.SerializedName

class CurrentWeather {
     lateinit var condition:Condition
     @SerializedName("temp_c")   lateinit var temp:String
     lateinit var  humidity:String
     @SerializedName("pressure_in")lateinit var pressure:String
}