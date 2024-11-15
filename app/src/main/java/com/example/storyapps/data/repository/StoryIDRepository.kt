package com.example.storyapps.data.repository

import com.example.storyapps.data.pref.UserPreference
import com.example.storyapps.data.retrofit.ApiService

class StoryIDRepository(
    private val apiService: ApiService,
    private val userPreference: UserPreference
) {

    suspend fun getStoryDetail(id: String) = apiService.getStoryDetail(id)

    companion object {
        @Volatile
        private var instance: StoryIDRepository? = null
        fun getInstance(
            apiService: ApiService,
            userPreference: UserPreference
        ): StoryIDRepository =
            instance ?: synchronized(this) {
                instance ?: StoryIDRepository(apiService, userPreference)
            }.also { instance = it }
    }
}