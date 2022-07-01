package com.bignerdranch.android.safeshopping

class TestWeatherRepository(): WeatherRepository() {

    companion object{

        fun getInstance():WeatherRepository{

            if(weatherRepository == null){
                weatherRepository= TestWeatherRepository()
            }
            return weatherRepository as WeatherRepository
        }
        fun reset() {
            weatherRepository = null
        }

    }

}