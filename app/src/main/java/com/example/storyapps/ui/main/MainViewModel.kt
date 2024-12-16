package com.example.storyapps.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
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

    val stories: LiveData<PagingData<ListStory>> =
        story.getStory().cachedIn(viewModelScope)

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
        try {
            val response = ApiConfig.getApiService(token).getStories()
            _listStory.value = response.listStory?.filterNotNull()
        } catch (e: Exception) {
            _listStory.value = null
        } finally {
            _isLoading.value = false
        }
    }
}