package com.example.storyapps.di

import android.content.Context
import android.util.Log
import androidx.paging.LOG_TAG
import com.example.storyapps.data.local.database.StoryDatabase
import com.example.storyapps.data.pref.UserPreference
import com.example.storyapps.data.pref.dataStore
import com.example.storyapps.data.repository.StoryIDRepository
import com.example.storyapps.data.repository.StoryRepository
import com.example.storyapps.data.repository.UserRepository
import com.example.storyapps.data.retrofit.ApiConfig
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

object Injection {
    fun provideRepository(context: Context): UserRepository {
        val pref = UserPreference.getInstance(context.dataStore)
        val user = runBlocking { pref.getSession().first() }
        val apiService = ApiConfig.getApiService(user.token)
        return UserRepository.getInstance(pref, apiService)
    }

    fun provideRepositoryStory(context: Context): StoryRepository {
        val pref = UserPreference.getInstance(context.dataStore)
        val user = runBlocking { pref.getSession().first() }
        val apiService = ApiConfig.getApiService(user.token)
        Log.d("StoryRemoteMediator", "provideRepositoryStory: ${user.token}")
        val storyDatabase = StoryDatabase.getDatabase(context)
        return StoryRepository.getInstance(apiService, pref, storyDatabase)
    }

    fun provideRepositoryStoryID(context: Context): StoryIDRepository {
        val pref = UserPreference.getInstance(context.dataStore)
        val user = runBlocking { pref.getSession().first() }
        val apiService = ApiConfig.getApiService(user.token)
        return StoryIDRepository.getInstance(apiService, pref)
    }
}