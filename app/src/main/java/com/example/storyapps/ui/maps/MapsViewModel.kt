package com.example.storyapps.ui.maps

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.storyapps.data.repository.StoryRepository
import com.example.storyapps.data.repository.UserRepository
import com.example.storyapps.data.response.ListStory
import com.example.storyapps.data.retrofit.ApiConfig
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

class MapsViewModel (private val repository: UserRepository, private val story : StoryRepository) : ViewModel() {
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _listStory = MutableLiveData<List<ListStory>?>()
    val listStory: LiveData<List<ListStory>?> = _listStory

    suspend fun getToken(): String {
        return repository.getSession().map { it.token }.first()
        Log.d("MapsViewModel", "getToken: ${repository.getSession().map { it.token }.first()}")
    }
    suspend fun getStoryWithLocation(token: String) {
        _isLoading.value = true
        try {
            val response = ApiConfig.getApiService(token).getStoriesWithLocation()
            _listStory.value = response.listStory?.filterNotNull()
            Log.d("MapsViewModel", "getStory: ${response.listStory}")
        } catch (e: Exception) {
            _listStory.value = null
        } finally {
            _isLoading.value = false
        }
    }
}