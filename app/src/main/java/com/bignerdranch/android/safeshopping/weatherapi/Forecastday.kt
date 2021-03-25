package com.bignerdranch.android.safeshopping.weatherapi

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import kotlinx.android.parcel.RawValue

@Parcelize
data class Forecastday(
    val date :String,
     val day :@RawValue Day,
     var astro:@RawValue Astro,
    @SerializedName("hour") val hours:@RawValue List<Hour>
): Parcelable
