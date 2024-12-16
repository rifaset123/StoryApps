package com.example.storyapps.data.local.database

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.storyapps.data.response.ListStory

@Dao
interface StoryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertQuote(quote: List<ListStory>)

    @Query("SELECT * FROM story")
    fun getAllQuote(): PagingSource<Int, ListStory>

    @Query("DELETE FROM story")
    suspend fun deleteAll()
}