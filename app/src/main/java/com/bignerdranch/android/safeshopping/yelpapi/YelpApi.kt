package com.bignerdranch.android.safeshopping.yelpapi

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface YelpApi {


    @GET("businesses/search")
    fun fetchShops(
            @Header("Authorization") authHeader:String,
            @Query("term")searchTerm:String,
            @Query("location")location:String): Call<YelpResponse>
}