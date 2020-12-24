package com.bignerdranch.android.safeshopping

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
import androidx.navigation.fragment.navArgs
import com.bignerdranch.android.safeshopping.weatherapi.CurrentWeather
import com.bignerdranch.android.safeshopping.weatherapi.Forecastday
import com.bignerdranch.android.safeshopping.weatherapi.WeatherResponse
import com.squareup.picasso.Picasso
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*


private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"
private const val TAG = "ShopFragment"


class ShopFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private val args: ShopFragmentArgs by navArgs()
    lateinit var tvName:TextView
    lateinit var imageView:ImageView
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

    private lateinit var shopFragmentViewModel: ShopFragmentViewModel
    var day0 = Calendar.getInstance()
    var day1 = Calendar.getInstance()
    var day2 = Calendar.getInstance()
    var day3 = Calendar.getInstance()
    var day4 = Calendar.getInstance()
    var day5 = Calendar.getInstance()
    var day6 = Calendar.getInstance()
    var day7 = Calendar.getInstance()
    var day8 = Calendar.getInstance()
    var day9 = Calendar.getInstance()
    val displayDateFromater = SimpleDateFormat("EEE MM-dd")
    val requestDateFormatter = SimpleDateFormat("yyyy-MM-dd")





    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
        shopFragmentViewModel = ViewModelProviders.of(this).get(ShopFragmentViewModel::class.java)
        day1.add(Calendar.DAY_OF_WEEK,1)
        day2.add(Calendar.DAY_OF_WEEK,2)
        day3.add(Calendar.DAY_OF_WEEK,3)
        day4.add(Calendar.DAY_OF_WEEK,4)
        day5.add(Calendar.DAY_OF_WEEK,5)
        day6.add(Calendar.DAY_OF_WEEK,6)
        day7.add(Calendar.DAY_OF_WEEK,7)
        day8.add(Calendar.DAY_OF_WEEK,8)
        day9.add(Calendar.DAY_OF_WEEK,9)
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
        tvCondition = view.findViewById(R.id.tvCondition)
        tvTemp = view.findViewById(R.id.tvTemp)
        tvDate = view.findViewById(R.id.tvDate)
        tvHumidity = view.findViewById(R.id.tvHumidity)
        tvPressure  = view.findViewById(R.id.tvPressure)
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

        shopFragmentViewModel.fetchShopWeather(args.shop.coordinates.latitude+","+args.shop.coordinates.longitude)//"24.7394478,46.8098221"

        shopFragmentViewModel.shopWeatherLiveData?.observe(
            viewLifecycleOwner,
            Observer {weatherRes->

                weather = weatherRes
                updateUi()
                showCurrentWeather(weather)

            })



    }

    override fun onStart() {
        super.onStart()
        btnNow.setOnClickListener{
            showCurrentWeather(weather)

        }

        btnDay0.setOnClickListener {
            shopFragmentViewModel.fetchShopWeatherByDay(args.shop.coordinates.latitude+","+
                    args.shop.coordinates.longitude,requestDateFormatter.format(day0.time))
            observeWeatherLiveData()

        }
        btnDay1.setOnClickListener {
            shopFragmentViewModel.fetchShopWeatherByDay(args.shop.coordinates.latitude+","+
                    args.shop.coordinates.longitude,requestDateFormatter.format(day1.time))
            observeWeatherLiveData()
        }
        btnDay2.setOnClickListener {
            shopFragmentViewModel.fetchShopWeatherByDay(args.shop.coordinates.latitude+","+
                    args.shop.coordinates.longitude,requestDateFormatter.format(day2.time))
            observeWeatherLiveData()

        }
        btnDay3.setOnClickListener {
            shopFragmentViewModel.fetchShopWeatherByDay(args.shop.coordinates.latitude+","+
                    args.shop.coordinates.longitude,requestDateFormatter.format(day3.time))
            observeWeatherLiveData()

        }
        btnDay4.setOnClickListener {
            shopFragmentViewModel.fetchShopWeatherByDay(args.shop.coordinates.latitude+","+
                    args.shop.coordinates.longitude,requestDateFormatter.format(day4.time))
            observeWeatherLiveData()

        }
        btnDay5.setOnClickListener {
            shopFragmentViewModel.fetchShopWeatherByDay(args.shop.coordinates.latitude+","+
                    args.shop.coordinates.longitude,requestDateFormatter.format(day5.time))
            observeWeatherLiveData()

        }
        btnDay6.setOnClickListener {
            shopFragmentViewModel.fetchShopWeatherByDay(args.shop.coordinates.latitude+","+
                    args.shop.coordinates.longitude,requestDateFormatter.format(day6.time))
            observeWeatherLiveData()

        }
        btnDay7.setOnClickListener {
            shopFragmentViewModel.fetchShopWeatherByDay(args.shop.coordinates.latitude+","+
                    args.shop.coordinates.longitude,requestDateFormatter.format(day7.time))
            observeWeatherLiveData()

        }
        btnDay8.setOnClickListener {
            shopFragmentViewModel.fetchShopWeatherByDay(args.shop.coordinates.latitude+","+
                    args.shop.coordinates.longitude,requestDateFormatter.format(day8.time))
            observeWeatherLiveData()

        }
        btnDay9.setOnClickListener {
            shopFragmentViewModel.fetchShopWeatherByDay(args.shop.coordinates.latitude+","+
                    args.shop.coordinates.longitude,requestDateFormatter.format(day9.time))
            observeWeatherLiveData()

        }

        btnHourly.setOnClickListener {
            
        }


    }
    private fun observeWeatherLiveData(){
        shopFragmentViewModel.shopWeatherLiveData?.observe(
            viewLifecycleOwner,
            Observer {weatherRes->
                var forecast = weatherRes.forecast
                showDayWeather(forecast.forecastday[0])

            })

    }
    private fun updateUi(){
        Picasso.get().load(args.shop.imageUrl).resize(370,350)
            .placeholder(R.drawable.ic_launcher_foreground)
            .into(imageView)
        tvName.text = args.shop.name
        ratingBar.rating = args.shop.rating.toFloat()
         btnDay1.text =displayDateFromater.format(day1.time)
        btnDay2.text = requestDateFormatter.format(day2.time)
        btnDay3.text =displayDateFromater.format(day3.time)
        btnDay4.text = requestDateFormatter.format(day4.time)
        btnDay5.text =displayDateFromater.format(day5.time)
        btnDay6.text = requestDateFormatter.format(day6.time)
        btnDay7.text =displayDateFromater.format(day7.time)
        btnDay8.text = requestDateFormatter.format(day8.time)
        btnDay9.text =displayDateFromater.format(day9.time)




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
        Picasso.get().load(args.shop.imageUrl).resize(370,350).placeholder(R.drawable.ic_launcher_foreground)
            .into(imageView)
       val day = forecastday.day
        val astro =forecastday.astro
        tvName.text = args.shop.name
        tvCondition.text =day.condition.text
        tvTemp.text = day.avgTemp
        tvHumidity.text = day.avgHumidity
        tvPressure.text= day.maxTemp
        tvDate.text = weather.forecast.forecastday[0].date
        ratingBar.rating = args.shop.rating.toFloat()
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