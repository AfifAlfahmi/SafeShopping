package com.bignerdranch.android.safeshopping.yelpapi

import android.os.Parcelable
import com.bignerdranch.android.safeshopping.Shop
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

data class YelpResponse (
    @SerializedName("total") val total:Int,
    @SerializedName("businesses") val shops: List<Shop>
)


@Parcelize
data class Category(
    val title:String
): Parcelable

@Parcelize
data class ShopLocation(
        @SerializedName("address1") val address:String

):Parcelable

