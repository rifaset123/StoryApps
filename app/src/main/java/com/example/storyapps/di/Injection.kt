package com.example.storyapps.di

import android.content.Context
import com.example.storyapps.data.pref.UserPreference
import com.example.storyapps.data.pref.dataStore
import com.example.storyapps.data.repository.UserRepository

object Injection {
    fun provideRepository(context: Context): UserRepository {
        val pref = UserPreference.getInstance(context.dataStore)
        return UserRepository.getInstance(pref)
    }
}