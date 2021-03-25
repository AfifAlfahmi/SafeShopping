package com.bignerdranch.android.safeshopping.yelpapi

import android.os.Parcelable
import com.bignerdranch.android.safeshopping.Shop
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

data class YelpResponse (
    val total:Int,
    @SerializedName("businesses") val shops: List<Shop>
)






