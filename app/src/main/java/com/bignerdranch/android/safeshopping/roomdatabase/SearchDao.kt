package com.bignerdranch.android.safeshopping.roomdatabase

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.bignerdranch.android.safeshopping.Search

@Dao
interface SearchDao {

    @Query("SELECT * FROM search ")
    fun getSearchTerms(): LiveData<List<Search>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun addSearchTerm(searchTerm: Search)

    @Query("DELETE FROM search ")
    fun deleteSearchTerms()
}