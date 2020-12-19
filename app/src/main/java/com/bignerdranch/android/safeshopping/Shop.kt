package com.bignerdranch.android.safeshopping

import android.os.Parcelable
import com.bignerdranch.android.safeshopping.yelpapi.Category
import com.bignerdranch.android.safeshopping.yelpapi.ShopLocation
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import kotlinx.android.parcel.RawValue

@Parcelize
data class  Shop(
        val name :String,
        val rating:Double,
        val price :String,
        val id:String,
        @SerializedName("review_count") val numReviews:Int,
        @SerializedName("distance") val distanceInMeters:Double,
        @SerializedName("image_url") val imageUrl:String,
        val categories:@RawValue List<Category>,
        val location:@RawValue ShopLocation,
        val coordinates:@RawValue Coordinates

):Parcelable