package com.bignerdranch.android.safeshopping.yelpapi

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Category(
    val title:String
): Parcelable