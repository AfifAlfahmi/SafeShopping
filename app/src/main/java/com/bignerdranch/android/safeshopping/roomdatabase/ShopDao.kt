package com.bignerdranch.android.safeshopping.roomdatabase

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.bignerdranch.android.safeshopping.Shop

@Dao
interface ShopDao {

    @Query("SELECT * FROM shop ")
    fun getShops(): LiveData<List<Shop>>

    @Query("SELECT * FROM shop ")
    fun getShopsList(): List<Shop>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun addShops(shopsList: List<Shop>)

    @Query("DELETE  FROM shop ")
    fun deleteShops()
}