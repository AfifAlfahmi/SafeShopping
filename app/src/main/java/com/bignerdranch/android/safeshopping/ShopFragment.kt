package com.bignerdranch.android.safeshopping

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import com.bignerdranch.android.safeshopping.weatherapi.CurrentWeather
import com.bignerdranch.android.safeshopping.weatherapi.WeatherResponse
import com.squareup.picasso.Picasso


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
    lateinit var ratingBar:RatingBar



    lateinit var currentWearher:CurrentWeather

    private lateinit var shopFragmentViewModel: ShopFragmentViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
        shopFragmentViewModel = ViewModelProviders.of(this).get(ShopFragmentViewModel::class.java)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
         val view = inflater.inflate(R.layout.fragment_shop, container, false)
        tvName = view.findViewById(R.id.tvName)
        imageView = view.findViewById(R.id.shopImageView)
        tvCondition = view.findViewById(R.id.tvCondition)
        tvTemp = view.findViewById(R.id.tvTemp)
        tvDate = view.findViewById(R.id.tvDate)
        ratingBar = view.findViewById(R.id.shopRatingBar)

        tvName.setOnClickListener{
            Navigation.findNavController(view).navigate(ShopFragmentDirections
                .actionShopFragmentToMapsFragment2())
        }

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)




        shopFragmentViewModel.fetchShopWeather(args.shop.coordinates.latitude+","+args.shop.coordinates.longitude)//"24.7394478,46.8098221"

        shopFragmentViewModel.shopWeatherLiveData?.observe(
            viewLifecycleOwner,
            Observer {weatherResponse ->

                updateUI(weatherResponse)

            })



    }
private fun updateUI(weatherResponse:WeatherResponse){
    Picasso.get().load(args.shop.imageUrl).resize(370,400).placeholder(R.drawable.ic_launcher_foreground)
        .into(imageView)
    tvName.text = args.shop.name
    currentWearher =weatherResponse.current
    tvCondition.text = currentWearher.condition.text
    tvTemp.text = currentWearher.temp
    tvDate.text = weatherResponse.forecast.forecastday[0].date
    ratingBar.rating = args.shop.rating.toFloat()


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