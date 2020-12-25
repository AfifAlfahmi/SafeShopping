package com.bignerdranch.android.safeshopping.weatherapi

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import kotlinx.android.parcel.RawValue

@Parcelize
data class Hour(
 @SerializedName("temp_c")  val temp:String,
     val time:String,
     val condition:@RawValue Condition,
    var isExpanded:Boolean = false
): Parcelable