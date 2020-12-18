package com.bignerdranch.android.safeshopping.yelpapi

import com.bignerdranch.android.safeshopping.Shop
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query
private const val API_KEY = "vNQd7Rt3Hs63t6kdpnGU2wdzQdkyH_EO2eblSMgT-MrnguULSNpGqgWmMJadD3x_MaA_cxM7mQtSNAG01rWoOh1wS7oazCi2x9fxpGkXiTq4Q9JFm28mDaIx_ufYX3Yx"

interface YelpApi {


    @GET("businesses/search")
    fun fetchShops(
            @Header("Authorization") authHeader:String,
            @Query("term")searchTerm:String,
            @Query("latitude")latitude:Double,
            @Query("longitude")longitude:Double,
            @Query("radius")radius:Int): Call<YelpResponse>

    @GET("businesses/{id}")  //WavvLdfdP6g8aZTtbBQHTw
    fun fetchShopDetails(
        @Header("Authorization") authHeader:String,
        @Path("id")id:String): Call<Shop>
}