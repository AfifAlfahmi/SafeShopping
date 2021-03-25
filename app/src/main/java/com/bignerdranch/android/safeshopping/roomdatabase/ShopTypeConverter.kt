package com.bignerdranch.android.safeshopping.roomdatabase

import androidx.room.TypeConverter
import com.bignerdranch.android.safeshopping.yelpapi.Coordinates
import com.bignerdranch.android.safeshopping.yelpapi.Category
import com.google.gson.Gson

class ShopTypeConverter {

    @TypeConverter
    fun jsonToCategories(json: String?): List<Category> {
        return Gson().fromJson(json, Array<Category>::class.java).toList()
    }

    @TypeConverter
    fun categoriesToJson(categories: List<Category>): String? {
        return Gson().toJson(categories)
    }

    @TypeConverter
    fun jsonToCoordinates(json: String?): Coordinates {
        return Gson().fromJson(json, Coordinates::class.java)
    }

    @TypeConverter
    fun coordinatesToJson(coordinates: Coordinates): String? {
        return Gson().toJson(coordinates)
    }
}