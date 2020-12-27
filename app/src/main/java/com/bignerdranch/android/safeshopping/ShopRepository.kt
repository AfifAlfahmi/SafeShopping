package com.bignerdranch.android.safeshopping

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.room.Room
import com.bignerdranch.android.safeshopping.roomdatabase.ShopDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import java.util.*
import java.util.concurrent.Executors

private const val DATABASE_NAME = "shop-database"
class ShopRepository private constructor(context: Context) {

    private val database : ShopDatabase = Room.databaseBuilder(
        context.applicationContext,ShopDatabase::class.java,DATABASE_NAME).build()
    private val shopDao = database.shopDao()
    private val favshopDao = database.favoriteshopDao()
    private val searchDao = database.searchDao()


    private val executor = Executors.newSingleThreadExecutor()



    fun getShops(): LiveData<List<Shop>> = shopDao.getShops()


    fun addShops(shopsList: List<Shop>) {
        executor.execute {
            shopDao.addShops(shopsList)
        }
    }
    fun deleteShop(id:String) {
        executor.execute {
            shopDao.deleteShop(id)
        }
    }

        fun addFavoriteShop(favShop:FavoriteShop){
            executor.execute{
                favshopDao.addFavoriteShop(favShop)
            }

        }
    fun getFavoritesShops(): LiveData<List<FavoriteShop>> = favshopDao.getFavoritesShops()

    fun addSearchTerm(searchTerm: Search){
        executor.execute{
            searchDao.addSearchTerm(searchTerm)
        }

    }
    fun getSearchTerms():LiveData<List<Search>> = searchDao.getSearchTerms()

    fun deleteSearchTerms(){
        executor.execute {
            searchDao.deleteSearchTerms()

        }
    }



    companion object {
        private var INSTANCE: ShopRepository? = null
        fun initialize(context: Context) {
            if (INSTANCE == null) {
                INSTANCE = ShopRepository(context)
            }
        }
        fun get(): ShopRepository {

            return INSTANCE ?:
            throw IllegalStateException()
        }
    }
}
