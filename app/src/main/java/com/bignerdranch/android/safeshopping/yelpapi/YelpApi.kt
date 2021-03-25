package com.bignerdranch.android.safeshopping.yelpapi

import com.bignerdranch.android.safeshopping.Shop
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query

interface YelpApi {


    @GET("businesses/search")
    fun fetchShops(
            @Header("Authorization") authHeader:String,
            @Query("latitude")latitude:Double,
            @Query("longitude")longitude:Double,
            @Query("radius")radius:Int): Call<YelpResponse>

    @GET("businesses/search")
    fun searchShops(
        @Header("Authorization") authHeader:String,
        @Query("latitude")latitude:Double,
        @Query("longitude")longitude:Double,
        @Query("term")searchTerm:String,
        @Query("radius")radius:Int): Call<YelpResponse>



}