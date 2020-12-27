package com.bignerdranch.android.safeshopping

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class  Search(
    @PrimaryKey val searchTerm:String
)