 package com.bignerdranch.android.safeshopping

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bignerdranch.android.safeshopping.weatherapi.*
import com.squareup.picasso.Picasso
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*


private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"
private const val TAG = "ShopFragment"
private const val DIALOG_HOURS = "DialogHours"


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
    lateinit var tvPressure:TextView

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
    val displayDateFromater = SimpleDateFormat("EEE MM-dd")
    val requestDateFormatter = SimpleDateFormat("yyyy-MM-dd")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
        shopFragmentViewModel = ViewModelProviders.of(this).get(ShopFragmentViewModel::class.java)
        shop = args.shop
        latitude = shop.coordinates.latitude
        longitude = shop.coordinates.longitude
        if(1>2) {

                    activity?.onBackPressedDispatcher?.addCallback(
                this,
                object : OnBackPressedCallback(true) {
                    override fun handleOnBackPressed() {
                        val navHostFragment =
                            activity?.supportFragmentManager?.findFragmentById(R.id.fragment) as NavHostFragment
                        val navController = navHostFragment.navController
                        navController.navigate(ShopFragmentDirections.actionShopFragmentToMapsFragment2())
                    }
                })
        }

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
         val view = inflater.inflate(R.layout.fragment_shop, container, false)
        tvName = view.findViewById(R.id.tvName)
        imageView = view.findViewById(R.id.shopImageView)
        imgViewMap = view.findViewById(R.id.imgViewMap)
        tvCondition = view.findViewById(R.id.tvCondition)
        tvTemp = view.findViewById(R.id.tvTemp)
        tvDate = view.findViewById(R.id.tvDate)
        tvHumidity = view.findViewById(R.id.tvHumidity)
        tvPressure  = view.findViewById(R.id.tvPressure)
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


        //shopFragmentViewModel.fetchShopWeatherByDay(args.shop.coordinates.latitude+","+args.shop.coordinates.longitude,"2020-12-31")
        tvName.text = shop.name
        tvCategory.text  = shop.categories[0].title

        shopFragmentViewModel.fetchShopWeather(latitude+","+longitude)//"24.7394478,46.8098221"

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

        imgViewMap.setOnClickListener {
            val uri = Uri.parse("geo:0,0?q="+latitude+","+longitude)
            //startActivity(Intent(Intent.ACTION_VIEW,uri))


           // val gmmIntentUri = Uri.parse("google.streetview:cbll=46.414382,10.013988")

            val mapIntent = Intent(Intent.ACTION_VIEW, uri)
            mapIntent.setPackage("com.google.android.apps.maps")
            startActivity(mapIntent)

        }
        super.onStart()
        btnNow.setOnClickListener{
            showCurrentWeather(weather)
        }
        btnDay0.setOnClickListener {
            fetchShopWeatherByDay(0)
        }
        btnDay1.setOnClickListener {
            fetchShopWeatherByDay(1)
        }
        btnDay2.setOnClickListener {
            fetchShopWeatherByDay(2)
        }
        btnDay3.setOnClickListener {
            fetchShopWeatherByDay(3)
        }
        btnDay4.setOnClickListener {
            fetchShopWeatherByDay(4)
        }
        btnDay5.setOnClickListener {
            fetchShopWeatherByDay(5)
        }
        btnDay6.setOnClickListener {
            fetchShopWeatherByDay(6)
        }
        btnDay7.setOnClickListener {
            fetchShopWeatherByDay(7)
        }
        btnDay8.setOnClickListener {
            fetchShopWeatherByDay(8)
        }
        btnDay9.setOnClickListener {
            fetchShopWeatherByDay(9)
        }
        btnHourly.setOnClickListener {
            var listOfHours = forecast.forecastday[0].hours
            val arrayListOfHours = arrayListOf<Hour>()
            arrayListOfHours.addAll(listOfHours)

            var fragmentManager = activity?.supportFragmentManager
            HoursListFragment.newInstance(arrayListOfHours).apply {
                if(fragmentManager != null){
                    show(fragmentManager, DIALOG_HOURS)
                }
            }

        }


    }
    private  fun fetchShopWeatherByDay(numOfDay:Int){
        shopFragmentViewModel.fetchShopWeatherByDay(latitude+","+
                longitude,requestDateFormatter.format(day(numOfDay).time))
        observeWeatherLiveData()
    }
    private  fun day(numOfDay:Int):Calendar{
        var day = Calendar.getInstance()
        day.add(Calendar.DAY_OF_WEEK,numOfDay)

        return day

    }
    private fun observeWeatherLiveData(){
        shopFragmentViewModel.shopWeatherLiveData?.observe(
            viewLifecycleOwner,
            Observer {weatherRes->
                 forecast = weatherRes.forecast
                showDayWeather(forecast.forecastday[0])

            })

    }
    private fun updateUi(){
        Picasso.get().load(args.shop.imageUrl).resize(385,350)
            .placeholder(R.drawable.ic_launcher_foreground)
            .into(imageView)
        tvName.text = args.shop.name
        ratingBar.rating = args.shop.rating.toFloat()
         btnDay1.text =displayDateFromater.format(day(1).time)
        btnDay2.text = displayDateFromater.format(day(2).time)
        btnDay3.text =displayDateFromater.format(day(3).time)
        btnDay4.text = displayDateFromater.format(day(4).time)
        btnDay5.text =displayDateFromater.format(day(5).time)
        btnDay6.text = displayDateFromater.format(day(6).time)
        btnDay7.text =displayDateFromater.format(day(7).time)
        btnDay8.text = displayDateFromater.format(day(8).time)
        btnDay9.text =displayDateFromater.format(day(9).time)




    }
private fun showCurrentWeather(weatherResponse:WeatherResponse){

    lateinit var currentWearher:CurrentWeather
    currentWearher =weatherResponse.current
    tvCondition.text = currentWearher.condition.text
    tvTemp.text = currentWearher.temp
    tvHumidity.text = currentWearher.humidity
    tvPressure.text= currentWearher.pressure
    tvDate.text = weatherResponse.forecast.forecastday[0].date
    Picasso.get().load("https:"+currentWearher.condition.icon).resize(370,350).placeholder(R.drawable.ic_launcher_foreground)
        .into(imgCondition)


}
    private fun showDayWeather(forecastday:Forecastday){
        Picasso.get().load(shop.imageUrl).resize(370,350).placeholder(R.drawable.ic_launcher_foreground)
            .into(imageView)
       val day = forecastday.day
        val astro =forecastday.astro
        tvCondition.text =day.condition.text
        tvTemp.text = day.avgTemp
        tvHumidity.text = day.avgHumidity
        tvPressure.text= day.maxTemp
        tvDate.text = forecastday.date
        ratingBar.rating = shop.rating.toFloat()
        Picasso.get().load("https:"+day.condition.icon).resize(370,350).placeholder(R.drawable.ic_launcher_foreground)
            .into(imgCondition)


    }

    companion object {


        fun newInstance(param1: String, param2: String) =
                ShopFragment().apply {
                    arguments = Bundle().apply {
                        putString(ARG_PARAM1, param1)
                        putString(ARG_PARAM2, param2)
                    }
                }
    }
}