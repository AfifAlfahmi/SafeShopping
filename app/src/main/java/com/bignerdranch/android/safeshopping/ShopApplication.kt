package com.bignerdranch.android.safeshopping

import android.app.Application

class ShopApplication : Application(){
    override fun onCreate() {
        super.onCreate()
        ShopRepository.initialize(this)

    }
}