package com.bignerdranch.android.safeshopping.yelpapi

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class Coordinates (
    var  latitude: String,
    var longitude: String
):Parcelable

