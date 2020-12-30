package com.bignerdranch.android.safeshopping



import android.util.Log
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModelProviders
import androidx.test.espresso.matcher.ViewMatchers
import com.bignerdranch.android.safeshopping.listener.WeatherResponseListener
import com.bignerdranch.android.safeshopping.weatherapi.WeatherApi
import com.bignerdranch.android.safeshopping.weatherapi.WeatherResponse

import junit.framework.Assert.assertEquals
import junit.framework.Assert.assertTrue
import org.hamcrest.CoreMatchers
import org.hamcrest.CoreMatchers.nullValue
import org.hamcrest.MatcherAssert
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.Is.`is`
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.*
import org.mockito.AdditionalMatchers.not
import org.mockito.Mockito.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
inline fun <reified T : Any> mock() = Mockito.mock(T::class.java)
@RunWith(JUnit4::class)
class WeatherReopsitoryTest {


    var lat ="23.34433"
    var long ="46.34433"

    private lateinit var weatherRepository: WeatherRepository
    @Captor
    private lateinit var searchCaptor: ArgumentCaptor<Callback<WeatherResponse>>

   @Mock
  private lateinit var weatherRes:Callback<WeatherResponse>

    @Mock
    private lateinit var retrofit: Retrofit
    @Mock
    private lateinit var weatherApi: WeatherApi
    @Mock
    private lateinit var weatherResponseListener: WeatherResponseListener



    @get:Rule
    val taskExecutorRule = InstantTaskExecutorRule()


@Before
fun setUp() {
    MockitoAnnotations.initMocks(this)


    weatherRepository =TestWeatherRepository.getInstance()

    `when`(retrofit.create(WeatherApi::class.java)).thenReturn(weatherApi)
    weatherRepository.addFetchWeatherListener(weatherResponseListener)

}

@After
fun tearDown() {

    reset(retrofit, weatherApi, weatherResponseListener)
    weatherRepository.removeFetchWeatherListener(weatherResponseListener)
    TestWeatherRepository.reset()
}



    @Test
    fun fetchPhotosListenerTriggeredOnSuccess() {




    }

    @Test
    fun fetchPhotosResopnceCodeOnSuccess() {

        val responseCall: Call<WeatherResponse> = mock()
        `when`(weatherApi.fetchWeather(ArgumentMatchers.anyString()))
            .thenReturn(responseCall)
        val flickrResponse: WeatherResponse = mock()
        weatherRepository.fetchWeather(lat+","+long)
        val response = Response.success(weatherRepository)
        var responseCode = response.code()

        assertThat(responseCode, CoreMatchers.equalTo(200))
    }

    @Test fun fetchTest (){

        var weatherRes  = weatherRepository.fetchWeather(lat+","+long)

        ViewMatchers.assertThat(weatherRes, CoreMatchers.`is`(CoreMatchers.notNullValue()))






    }


}

