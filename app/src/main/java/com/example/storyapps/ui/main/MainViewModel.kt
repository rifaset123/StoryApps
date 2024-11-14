package com.example.storyapps.ui.main

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.storyapps.data.pref.UserModel
import com.example.storyapps.data.repository.StoryRepository
import com.example.storyapps.data.repository.UserRepository
import com.example.storyapps.data.response.ListStory
import com.example.storyapps.data.retrofit.ApiConfig
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class MainViewModel(private val repository: UserRepository, private val story : StoryRepository) : ViewModel() {
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _listStory = MutableLiveData<List<ListStory>?>()
    val listStory: LiveData<List<ListStory>?> = _listStory

    fun getSession(): LiveData<UserModel> {
        return repository.getSession().asLiveData()
    }

    suspend fun getToken(): String {
        return repository.getSession().map { it.token }.first()
    }

    fun logout() {
        viewModelScope.launch {
            repository.logout()
        }
    }

    suspend fun getStory(token: String) {
        _isLoading.value = true
        Log.d("MainViewModelqqweqeqwe", "getStory: $token")
        try {
            val response = ApiConfig.getApiService(token).getStories()
            Log.d("MainViewModelqqweqeqwe", "getStory: $response")
            _listStory.value = response.listStory?.filterNotNull()
        } catch (e: Exception) {
            _listStory.value = null
            Log.d("MainViewModelqqweqeqwe", "gagal")
            if (e is retrofit2.HttpException) {
                Log.e("MainViewModelqqweqeqwe", "HTTP error: ${e.code()} ${e.message()}", e)
            } else {
                Log.e("MainViewModelqqweqeqwe", "Error fetching stories", e)
            }
        } finally {
            _isLoading.value = false
        }
    }
}