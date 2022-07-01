package com.bignerdranch.android.safeshopping

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.room.Room
import com.bignerdranch.android.safeshopping.roomdatabase.ShopDatabase
import com.bignerdranch.android.safeshopping.yelpapi.YelpApi
import com.bignerdranch.android.safeshopping.yelpapi.YelpResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*
import java.util.concurrent.Executors

private const val DATABASE_NAME = "shop-database"
private const val BASE_URL = "https://api.yelp.com/v3/"
private const val API_KEY = "Bearer vNQd7Rt3Hs63t6kdpnGU2wdzQdkyH_EO2eblSMgT-MrnguULSN" +
        "pGqgWmMJadD3x_MaA_cxM7mQtSNAG01rWoOh1wS7oazCi2x9fxpGkXiTq4Q9JFm28mDaIx_ufYX3Yx"
private const val RADIUS = 40000
private const val TAG = "ShopRepository"

class ShopRepository private constructor(context: Context) {

    private val database: ShopDatabase = Room.databaseBuilder(
        context.applicationContext, ShopDatabase::class.java, DATABASE_NAME
    ).build()
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

    fun deleteShops() {
        executor.execute {
            shopDao.deleteShops()
        }
    }

    fun addFavoriteShop(favShop: FavoriteShop) {
        executor.execute {
            favshopDao.addFavoriteShop(favShop)
        }
    }

    fun deleteFavoriteShop(id: String) {
        executor.execute {
            favshopDao.deleteFavoriteShop(id)
        }
    }

    fun getFavoritesShops(): LiveData<List<FavoriteShop>> = favshopDao.getFavoritesShops()

    fun addSearchTerm(searchTerm: Search) {
        executor.execute {
            searchDao.addSearchTerm(searchTerm)
        }
    }

    fun getSearchTerms(): LiveData<List<Search>> = searchDao.getSearchTerms()

    fun deleteSearchTerms() {
        executor.execute {
            searchDao.deleteSearchTerms()
        }
    }

    fun fetchShops(lat: Double?, long: Double?): LiveData<List<Shop>> {
        val responseLiveData: MutableLiveData<List<Shop>> = MutableLiveData()
        val shops = mutableListOf<Shop>()
        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val yelpApi = retrofit.create(YelpApi::class.java)
        if (lat != null && long != null) {
            yelpApi.fetchShops(
                 API_KEY,
                lat, long, RADIUS
            ).enqueue(object : Callback<YelpResponse> {
                override fun onResponse(
                    call: Call<YelpResponse>,
                    response: Response<YelpResponse>
                ) {
                    val body = response.body()
                    if (body == null) {
                        return
                    }
                    var shopList: List<Shop> = body.shops
                    shopList = shopList.filterNot {
                        it.imageUrl.isEmpty()
                    }
                    shops.addAll(shopList)
                    responseLiveData.value = shops
                }

                override fun onFailure(call: Call<YelpResponse>, t: Throwable) {
                }
            })
        }
        return responseLiveData
    }

    fun searchShops(lat: Double?, long: Double?, searchTerm: String): LiveData<List<Shop>> {
        val responseLiveData: MutableLiveData<List<Shop>> = MutableLiveData()
        val shops = mutableListOf<Shop>()
        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val yelpApi = retrofit.create(YelpApi::class.java)
        if (lat != null && long != null) {
            yelpApi.searchShops(API_KEY, lat, long, searchTerm, RADIUS)
                .enqueue(object : Callback<YelpResponse> {
                    override fun onResponse(
                        call: Call<YelpResponse>, response:
                        Response<YelpResponse>
                    ) {
                        val body = response.body()
                        if (body == null) {
                            return
                        }
                        shops.addAll(body.shops)
                        responseLiveData.value = shops
                    }

                    override fun onFailure(call: Call<YelpResponse>, t: Throwable) {
                    }
                })
        }
        return responseLiveData
    }

    companion object {
        var instance: ShopRepository? = null
        fun initialize(context: Context) {
            if (instance == null) {
                instance = ShopRepository(context)
            }
        }

        fun get(): ShopRepository {
            return instance ?: throw IllegalStateException()
        }
    }
}
