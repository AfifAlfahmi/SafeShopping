package com.bignerdranch.android.safeshopping.roomdatabase

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.bignerdranch.android.safeshopping.Shop

@Database(entities = [ Shop::class ], version=1,exportSchema = false)
@TypeConverters(ShopTypeConverter::class)
abstract class ShopDatabase : RoomDatabase() {
    abstract fun shopDao(): ShopDao

}