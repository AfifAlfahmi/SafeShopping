package com.bignerdranch.android.safeshopping

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.bignerdranch.android.safeshopping.yelpapi.Category
import com.bignerdranch.android.safeshopping.yelpapi.ShopLocation
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import kotlinx.android.parcel.RawValue
@Entity
@Parcelize
data class  FavoriteShop(
        @PrimaryKey val id:String,
        val name :String,
        val color:String,
        val title :String,
        val price :String,
        val rating:Double,
        @SerializedName("review_count") val numReviews:Int,
        @SerializedName("distance") val distanceInMeters:Double,
        @SerializedName("image_url") val imageUrl:String,
        val categories:@RawValue List<Category>,
        val location:@RawValue ShopLocation,
        val coordinates:@RawValue Coordinates

):Parcelable