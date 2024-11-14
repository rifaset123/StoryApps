package com.example.storyapps.data.repository

import com.example.storyapps.data.pref.UserPreference
import com.example.storyapps.data.response.RegisterResponse
import com.example.storyapps.data.retrofit.ApiService

class StoryRepository(
    private val apiService: ApiService,
    private val userPreference: UserPreference
) {
    companion object {
        @Volatile
        private var instance: StoryRepository? = null
        fun getInstance(
            apiService: ApiService,
            userPreference: UserPreference
        ): StoryRepository =
            instance ?: synchronized(this) {
                instance ?: StoryRepository(apiService, userPreference)
            }.also { instance = it }
    }
}