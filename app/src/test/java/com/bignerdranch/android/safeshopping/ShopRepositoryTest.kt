package com.bignerdranch.android.safeshopping

import android.content.Context
import android.os.Build
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.bignerdranch.android.safeshopping.roomdatabase.SearchDao
import com.bignerdranch.android.safeshopping.roomdatabase.ShopDao
import com.bignerdranch.android.safeshopping.roomdatabase.ShopDatabase
import com.bignerdranch.android.safeshopping.weatherapi.WeatherApi
import com.bignerdranch.android.safeshopping.weatherapi.WeatherResponse
import com.bignerdranch.android.safeshopping.yelpapi.Category
import com.bignerdranch.android.safeshopping.yelpapi.ShopLocation
import com.bignerdranch.android.safeshopping.yelpapi.YelpApi
import com.bignerdranch.android.safeshopping.yelpapi.YelpResponse
import com.github.tomakehurst.wiremock.client.WireMock.equalTo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.CoreMatchers
import org.junit.After
import org.junit.Before
import org.junit.runner.RunWith
import java.io.IOException
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Rule
import org.junit.Test
import org.mockito.ArgumentMatchers
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.robolectric.annotation.Config
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


@RunWith(AndroidJUnit4::class)
@Config(sdk = [Build.VERSION_CODES.O_MR1])
class ShopRepositoryTest {

    private lateinit var db: ShopDatabase
    private lateinit var userDao: SearchDao
    private lateinit var shoDao: ShopDao
    val shopCategory = "coffee"
    val address = "Riyadh"
    val lat = "24.433553"
    val long = "46.443432"
    val firstShopId ="1"
    val secondShopId ="2"
    val shopName ="Mcdonalds"
    val rating = 3.5
    val review = 33
    val distance = 500.0
    val imgUrl ="https"
    val price = "30"
    var expectedShopName ="Mcdonalds"
    var firsItem = 0
    var expectedSize = 2
    private val shopRepository = ShopRepository.get()




    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(
            context, ShopDatabase::class.java).build()
        userDao = db.searchDao()
        shoDao = db.shopDao()


    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }


    @Test
    @Throws(Exception::class)
    fun  `add search term and check the values`() {
        val firstSearchTerm = Search("Afif")
        val secondSearchTerm = Search("Alfahmi")
        val expectedSearchTerm = "Afif"

      runBlocking{
          launch(Dispatchers.IO){
              userDao.addSearchTerm(firstSearchTerm)
              userDao.addSearchTerm(secondSearchTerm)
              val searchList = userDao.getSearchList()
              assertThat(searchList[firsItem].searchTerm, CoreMatchers.equalTo(expectedSearchTerm))
              assertThat(searchList.size ,CoreMatchers.equalTo(expectedSize ))

          }

      }

        }

    @Test
    @Throws(Exception::class)
    fun `add list of Shops and check the list size `() {

        val category1= Category(shopCategory)
        val category2= Category(shopCategory)
        val listOfCategory = listOf(category1,category2)
        val shopLocation = ShopLocation(address)
        val coordinates =Coordinates(lat,long)


        val shop1 = Shop(firstShopId,shopName,rating,
            price,review,distance,imgUrl
            ,listOfCategory,shopLocation,coordinates)
        val shop2 = Shop(secondShopId,shopName,rating,
            price,review,distance,imgUrl
            ,listOfCategory,shopLocation,coordinates)
        val listOfShops = listOf(shop1,shop2)



        runBlocking{
            launch(Dispatchers.IO){
                shoDao.addShops(listOfShops)

                val shopsList = shoDao.getShopsList()
                assertThat(shopsList[firsItem].name, CoreMatchers.equalTo(expectedShopName))
                assertThat(shopsList .size ,CoreMatchers.equalTo(expectedSize ))

            }

        }

    }


    @Test
    @Throws(Exception::class)
    fun `check Yelp response list`(){

        val yelpResponse  = shopRepository.fetchShops()

        ViewMatchers.assertThat(yelpResponse, CoreMatchers.`is`(CoreMatchers.notNullValue()))
    }




}