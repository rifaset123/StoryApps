package com.example.storyapps.data.repository

import androidx.lifecycle.LiveData
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.example.storyapps.data.local.database.StoryDatabase
import com.example.storyapps.data.local.database.StoryRemoteMediator
import com.example.storyapps.data.paging.StoryPagingSource
import com.example.storyapps.data.pref.UserPreference
import com.example.storyapps.data.response.ListStory
import com.example.storyapps.data.retrofit.ApiService

class StoryRepository(
    private val apiService: ApiService,
    private val userPreference: UserPreference,
    private val storyDatabase: StoryDatabase
) {
    companion object {
        @Volatile
        private var instance: StoryRepository? = null
        fun getInstance(
            apiService: ApiService,
            userPreference: UserPreference,
            storyDatabase: StoryDatabase
        ): StoryRepository =
            instance ?: synchronized(this) {
                instance ?: StoryRepository(apiService, userPreference, storyDatabase)
            }.also { instance = it }
    }

    fun getStory(): LiveData<PagingData<ListStory>> {
        @OptIn(ExperimentalPagingApi::class)
        return Pager(
            config = PagingConfig(
                pageSize = 5
            ),
            remoteMediator = StoryRemoteMediator(storyDatabase, apiService),
            pagingSourceFactory = {
//                StoryPagingSource(apiService)
                storyDatabase.storyDao().getAllQuote()
            }
        ).liveData
    }
}