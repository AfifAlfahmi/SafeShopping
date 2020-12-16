package com.bignerdranch.android.safeshopping.yelpapi

import com.bignerdranch.android.safeshopping.Shop
import com.google.gson.annotations.SerializedName

data class YelpResponse (
    @SerializedName("total") val total:Int,
    @SerializedName("businesses") val shops: List<Shop>
)



data class Category(
        val title:String
)

data class ShopLocation(
        @SerializedName("address1") val address:String

)

