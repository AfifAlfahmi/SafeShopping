package com.bignerdranch.android.safeshopping

import com.bignerdranch.android.safeshopping.yelpapi.Category
import com.bignerdranch.android.safeshopping.yelpapi.ShopLocation
import com.google.gson.annotations.SerializedName

data class  Shop(
        val name :String,
        val rating:Double,
        val price :String,
        @SerializedName("review_count") val numReviews:Int,
        @SerializedName("distance") val distanceInMeters:Double,
        @SerializedName("image_url") val imageUrl:String,
        val categories:List<Category>,
        val location: ShopLocation

){
    fun displayDistance():String{
        val milesPerMeter = 0.000621371
        val distanceInMiles = "%2f".format(distanceInMeters * milesPerMeter)
        return "$distanceInMiles mi"
    }
}