package com.bignerdranch.android.safeshopping.roomdatabase

import androidx.room.TypeConverter
import com.bignerdranch.android.safeshopping.Coordinates
import com.bignerdranch.android.safeshopping.yelpapi.Category
import com.bignerdranch.android.safeshopping.yelpapi.ShopLocation
import com.google.gson.Gson
import java.util.*

class ShopTypeConverter {


    @TypeConverter//List<String>
    fun jsonToCategories(json: String?): List<Category> {
        return Gson().fromJson(json, Array<Category>::class.java).toList()
    }
    @TypeConverter
    fun categoriesToJson(categories: List<Category>): String? {
        return Gson().toJson(categories)
    }

    @TypeConverter
    fun jsonToLocation(json: String?):ShopLocation {
        return Gson().fromJson(json, ShopLocation::class.java)
    }

    @TypeConverter
    fun locationToJson(location: ShopLocation): String? {
        return Gson().toJson(location)
    }

    @TypeConverter
    fun jsonToCoordinates(json: String?): Coordinates {
        return Gson().fromJson(json, Coordinates::class.java)
    }

    @TypeConverter
    fun coordinatesToJson(coordinates: Coordinates): String? {
        return Gson().toJson(coordinates)
    }


//    @TypeConverter
//    fun fromCategory(categories: Category?): String? {
//        return categories?.title
//    }
//    @TypeConverter
//    fun toCategory(title: String?): Category? {
//        return title?.let {
//            Category(it)
//        }
//    }
}