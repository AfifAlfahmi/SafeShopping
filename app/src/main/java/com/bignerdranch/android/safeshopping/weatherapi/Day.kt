package com.bignerdranch.android.safeshopping.weatherapi

import com.google.gson.annotations.SerializedName

class Day {
    @SerializedName("maxtemp_c") lateinit var maxTemp:String
    @SerializedName("mintemp_c") lateinit var minTemp:String
}