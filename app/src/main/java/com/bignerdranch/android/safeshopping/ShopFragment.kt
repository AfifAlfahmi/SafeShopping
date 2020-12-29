 package com.bignerdranch.android.safeshopping

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.core.content.ContextCompat.getColor
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.navArgs
import com.bignerdranch.android.safeshopping.weatherapi.*
import com.squareup.picasso.Picasso
import java.text.SimpleDateFormat
import java.util.*


 private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"
private const val TAG = "ShopFragment"
private const val DIALOG_HOURS = "DialogHours"
 private const val KILO = 1000
 private const val FIRST_ITEM = 0
 private const val DAY_0 = 0
 private const val DAY_1 = 1
 private const val DAY_2 = 2
 private const val DAY_3 = 3
 private const val DAY_4 = 4
 private const val DAY_5 = 5
 private const val DAY_6 = 6
 private const val DAY_7 = 7
 private const val DAY_8 = 8
 private const val DAY_9 = 9
 private const val SHOP_IMG_WIDTH = 410
 private const val SHOP_IMG_HEIGHT = 350
 private const val WEATHER_ICON_WIDTH = 370
 private const val WEATHER_ICON_HEIGHT = 350
 private const val TEMP_SYMBOL =8451


 class ShopFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private val args: ShopFragmentArgs by navArgs()
    lateinit var tvName:TextView
    lateinit var tvCategory:TextView

    lateinit var imageView:ImageView
    lateinit var imgViewMap:ImageView

    lateinit var tvCondition:TextView
    lateinit var tvTemp:TextView
    lateinit var tvDate:TextView
    lateinit var tvHumidity:TextView
    lateinit var tvDistance:TextView
    lateinit var tvMinTemp:TextView


    lateinit var btnNow:Button
    lateinit var btnDay0:Button
    lateinit var btnDay1:Button
    lateinit var btnDay2:Button
    lateinit var btnDay3:Button
    lateinit var btnDay4:Button
    lateinit var btnDay5:Button
    lateinit var btnDay6:Button
    lateinit var btnDay7:Button
    lateinit var btnDay8:Button
    lateinit var btnDay9:Button
    lateinit var btnHourly:Button
    lateinit var ratingBar:RatingBar
    lateinit var imgCondition:ImageView
    lateinit var weather:WeatherResponse
    lateinit var forecast:Forecast
    private lateinit var shopFragmentViewModel: ShopFragmentViewModel
    private lateinit var shop:Shop
    private lateinit var latitude:String
    private lateinit var longitude:String
    val displayDateFromater = SimpleDateFormat()
    val requestDateFormatter = SimpleDateFormat()
    lateinit var imgViewFavorite:ImageView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
        displayDateFromater.applyPattern(getString(R.string.display_date_pattern))
        requestDateFormatter.applyPattern(getString(R.string.request_date_pattern))
        shopFragmentViewModel = ViewModelProviders.of(this).get(ShopFragmentViewModel::class.java)
        shop = args.shop
        latitude = shop.coordinates.latitude
        longitude = shop.coordinates.longitude


    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
         val view = inflater.inflate(R.layout.fragment_shop, container, false)
        tvName = view.findViewById(R.id.tvName)
        imageView = view.findViewById(R.id.shopImageView)
        imgViewMap = view.findViewById(R.id.imgViewMap)
        tvCondition = view.findViewById(R.id.tvCondition)
        imgViewFavorite = view.findViewById(R.id.imgFavorite)
        tvTemp = view.findViewById(R.id.tvTemp)
        tvDate = view.findViewById(R.id.tvSunrise)
        tvHumidity = view.findViewById(R.id.tvHumidity)
        tvDistance  = view.findViewById(R.id.tvDistance)
        tvMinTemp = view.findViewById(R.id.tvMinTemp)
        tvCategory = view.findViewById(R.id.tvShopCategory)
        ratingBar = view.findViewById(R.id.shopRatingBar)
        imgCondition = view.findViewById(R.id.imgCondition)
        btnNow  = view.findViewById(R.id.btnNow)
        btnDay0  = view.findViewById(R.id.btnDay0)
        btnDay1  = view.findViewById(R.id.btnDay1)
        btnDay2  = view.findViewById(R.id.btnDay2)
        btnDay3  = view.findViewById(R.id.btnDay3)
        btnDay4  = view.findViewById(R.id.btnDay4)
        btnDay5  = view.findViewById(R.id.btnDay5)
        btnDay6  = view.findViewById(R.id.btnDay6)
        btnDay7  = view.findViewById(R.id.btnDay7)
        btnDay8  = view.findViewById(R.id.btnDay8)
        btnDay9  = view.findViewById(R.id.btnDay9)
        btnHourly  = view.findViewById(R.id.btnHourly)

        tvName.setOnClickListener{
        }
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        tvName.text = shop.name
        tvCategory.text  = getString(R.string.category)+shop.categories[FIRST_ITEM].title
        val distanceInKilo = shop.distanceInMeters/ KILO
        val roundedDistanceInKilo = String.format(getString(R.string.kilo_format), distanceInKilo)
        tvDistance.text = roundedDistanceInKilo+getString(R.string.km)

        shopFragmentViewModel.fetchShopWeather(latitude+getString(R.string.comma)+longitude)

        shopFragmentViewModel.shopWeatherLiveData?.observe(
            viewLifecycleOwner,
            Observer {weatherRes->

                weather = weatherRes
                forecast = weatherRes.forecast
                updateUi()
                showCurrentWeather(weather)
            })
    }
    override fun onStart() {
        super.onStart()

        imgViewMap.setOnClickListener {
            val uri = Uri.parse(getString(R.string.geo_args)+
                    latitude+getString(R.string.comma)+longitude)


            val mapIntent = Intent(Intent.ACTION_VIEW, uri)
            mapIntent.setPackage(getString(R.string.map_package))
            startActivity(mapIntent)

        }
        imgViewFavorite.setOnClickListener {
            val fragmentManager = activity?.supportFragmentManager as FragmentManager
            FavoriteFragment.newInstance(shop).apply {
                if(fragmentManager != null){
                    show(fragmentManager, DIALOG_HOURS)
                }
            }

        }
        btnNow.setOnClickListener{
            showCurrentWeather(weather)
            changeBtnsTextColor()
            btnNow.setTextColor(getColor(requireContext(),R.color.purple_700))
        }
        btnDay0.setOnClickListener {
            fetchShopWeatherByDay(DAY_0)
            changeBtnsTextColor()
            btnDay0.setTextColor(getColor(requireContext(),R.color.purple_700))
        }
        btnDay1.setOnClickListener {
            fetchShopWeatherByDay(DAY_1)
            changeBtnsTextColor()
            btnDay1.setTextColor(getColor(requireContext(),R.color.purple_700))

        }
        btnDay2.setOnClickListener {
            fetchShopWeatherByDay(DAY_2)
            changeBtnsTextColor()
            btnDay2.setTextColor(getColor(requireContext(),R.color.purple_700))
        }
        btnDay3.setOnClickListener {
            fetchShopWeatherByDay(DAY_3)
            changeBtnsTextColor()
            btnDay3.setTextColor(getColor(requireContext(),R.color.purple_700))
        }
        btnDay4.setOnClickListener {
            fetchShopWeatherByDay(DAY_4)
            changeBtnsTextColor()
            btnDay4.setTextColor(getColor(requireContext(),R.color.purple_700))
        }
        btnDay5.setOnClickListener {
            fetchShopWeatherByDay(DAY_5)
            changeBtnsTextColor()
            btnDay5.setTextColor(getColor(requireContext(),R.color.purple_700))
        }
        btnDay6.setOnClickListener {
            fetchShopWeatherByDay(DAY_6)
            changeBtnsTextColor()
            btnDay6.setTextColor(getColor(requireContext(),R.color.purple_700))
        }
        btnDay7.setOnClickListener {
            fetchShopWeatherByDay(DAY_7)
            changeBtnsTextColor()
            btnDay7.setTextColor(getColor(requireContext(),R.color.purple_700))
        }
        btnDay8.setOnClickListener {
            fetchShopWeatherByDay(DAY_8)
            changeBtnsTextColor()
            btnDay8.setTextColor(getColor(requireContext(),R.color.purple_700))
        }
        btnDay9.setOnClickListener {
            fetchShopWeatherByDay(DAY_9)
            changeBtnsTextColor()
            btnDay9.setTextColor(getColor(requireContext(),R.color.purple_700))
        }
        btnHourly.setOnClickListener {
            val listOfHours = forecast.forecastday[FIRST_ITEM].hours
            val arrayListOfHours = arrayListOf<Hour>()
            arrayListOfHours.addAll(listOfHours)

            val fragmentManager = activity?.supportFragmentManager as FragmentManager
            HoursListFragment.newInstance(arrayListOfHours).apply {
                if(fragmentManager != null){
                    show(fragmentManager, DIALOG_HOURS)
                }
            }

        }


    }
    private  fun fetchShopWeatherByDay(numOfDay:Int){
        shopFragmentViewModel.fetchShopWeatherByDay(latitude+getString(R.string.comma)+
                longitude,requestDateFormatter.format(day(numOfDay).time))
        observeWeatherLiveData()
    }

    private fun changeBtnsTextColor(){
        btnNow.setTextColor(getColor(requireContext(),R.color.day_btn_color))
        btnDay0.setTextColor(getColor(requireContext(),R.color.day_btn_color))
        btnDay1.setTextColor(getColor(requireContext(),R.color.day_btn_color))
        btnDay2.setTextColor(getColor(requireContext(),R.color.day_btn_color))
        btnDay3.setTextColor(getColor(requireContext(),R.color.day_btn_color))
        btnDay4.setTextColor(getColor(requireContext(),R.color.day_btn_color))
        btnDay5.setTextColor(getColor(requireContext(),R.color.day_btn_color))
        btnDay6.setTextColor(getColor(requireContext(),R.color.day_btn_color))
        btnDay7.setTextColor(getColor(requireContext(),R.color.day_btn_color))
        btnDay8.setTextColor(getColor(requireContext(),R.color.day_btn_color))
        btnDay9.setTextColor(getColor(requireContext(),R.color.day_btn_color))

    }
    private  fun day(numOfDay:Int):Calendar{
        val day = Calendar.getInstance()
        day.add(Calendar.DAY_OF_WEEK,numOfDay)

        return day

    }
    private fun observeWeatherLiveData(){
        shopFragmentViewModel.shopWeatherLiveData?.observe(
            viewLifecycleOwner,
            Observer {weatherRes->
                 forecast = weatherRes.forecast
                showDayWeather(forecast.forecastday[FIRST_ITEM])

            })

    }
    private fun updateUi(){

        Picasso.get().load(shop.imageUrl).resize(SHOP_IMG_WIDTH,SHOP_IMG_HEIGHT)
            .placeholder(R.drawable.ic_launcher_foreground)
            .into(imageView)
        tvName.text = shop.name

        ratingBar.rating = args.shop.rating.toFloat()
         btnDay1.text =displayDateFromater.format(day(DAY_1).time)
        btnDay2.text = displayDateFromater.format(day(DAY_2).time)
        btnDay3.text =displayDateFromater.format(day(DAY_3).time)
        btnDay4.text = displayDateFromater.format(day(DAY_4).time)
        btnDay5.text =displayDateFromater.format(day(DAY_5).time)
        btnDay6.text = displayDateFromater.format(day(DAY_6).time)
        btnDay7.text =displayDateFromater.format(day(DAY_7).time)
        btnDay8.text = displayDateFromater.format(day(DAY_8).time)
        btnDay9.text =displayDateFromater.format(day(DAY_9).time)




    }
private fun showCurrentWeather(weatherResponse:WeatherResponse){

    lateinit var currentWearher:CurrentWeather
    currentWearher =weatherResponse.current
    tvCondition.text = currentWearher.condition.text
    Log.d(TAG,currentWearher.condition.text)
    tvTemp.text = currentWearher.temp+TEMP_SYMBOL.toChar()
    tvHumidity.text = currentWearher.humidity+" H"
    tvDate.text = currentWearher.pressure+" P"
    tvMinTemp.visibility = View.GONE
    tvHumidity.visibility = View.VISIBLE

    Picasso.get().load(getString(R.string.base_url)+currentWearher.condition.icon)
        .resize(WEATHER_ICON_WIDTH,WEATHER_ICON_HEIGHT).placeholder(R.drawable.ic_launcher_foreground)
        .into(imgCondition)


}
    private fun showDayWeather(forecastday:Forecastday){

       val day = forecastday.day
        if(day.condition.text == getString(R.string.patchy_rain_text)){
            tvCondition.text = getString(R.string.patchy_rain)
        }

        Log.d(TAG,day.condition.text)

        tvTemp.text = getString(R.string.max)+day.maxTemp+ TEMP_SYMBOL.toChar()
        tvHumidity.visibility = View.GONE

        tvDate.text = getString(R.string.sunrise)+forecastday.astro.sunrise
        tvMinTemp.text = getString(R.string.min)+day.minTemp+ TEMP_SYMBOL.toChar()
        tvMinTemp.visibility = View.VISIBLE

        ratingBar.rating = shop.rating.toFloat()
        Picasso.get().load(getString(R.string.base_url)+day.condition.icon)
            .resize(WEATHER_ICON_WIDTH, WEATHER_ICON_HEIGHT)
            .placeholder(R.drawable.ic_launcher_foreground)
            .into(imgCondition)


    }


}