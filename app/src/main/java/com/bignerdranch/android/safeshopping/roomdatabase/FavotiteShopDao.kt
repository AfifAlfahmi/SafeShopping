package com.bignerdranch.android.safeshopping.roomdatabase

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.bignerdranch.android.safeshopping.FavoriteShop
import com.bignerdranch.android.safeshopping.Shop

@Dao
interface FavotiteShopDao {

    @Query("SELECT * FROM favoriteShop  ")
    fun getFavoritesShops(): LiveData<List<FavoriteShop>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun addFavoriteShop(favShop: FavoriteShop)

    @Query("DELETE  FROM favoriteshop WHERE id=(:id)")
    fun deleteShop(id: String)
}