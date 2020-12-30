package com.bignerdranch.android.safeshopping

import android.content.Context
import android.os.Build
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.bignerdranch.android.safeshopping.roomdatabase.SearchDao
import com.bignerdranch.android.safeshopping.roomdatabase.ShopDatabase
import com.bignerdranch.android.safeshopping.weatherapi.WeatherResponse
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
import org.robolectric.annotation.Config
import retrofit2.Response


@RunWith(AndroidJUnit4::class)
@Config(sdk = [Build.VERSION_CODES.O_MR1])
class ShopRepositoryTest {


    private lateinit var userDao: SearchDao
    private lateinit var db: ShopDatabase

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(
            context, ShopDatabase::class.java).build()
        userDao = db.searchDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }


    @Test
    @Throws(Exception::class)
    fun writeUserAndReadInList() {


        var searchTerm = Search("Afif")
        var searchTerm2 = Search("Alfahmi")

      runBlocking{
          launch(Dispatchers.IO){
              userDao.addSearchTerm(searchTerm)
              userDao.addSearchTerm(searchTerm2)
              val byName = userDao.getSearchList()
              assertThat(byName[0].searchTerm, CoreMatchers.equalTo(searchTerm.searchTerm))
              assertThat(byName.size ,CoreMatchers.equalTo(2 ))

          }

      }

        }











}