package com.example.storyapps.ui.detail

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.storyapps.data.repository.StoryIDRepository
import com.example.storyapps.data.response.Story
import com.example.storyapps.data.response.StoryByIDResponse
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailViewModel(private val repository: StoryIDRepository) : ViewModel() {

    private val _storyDetail = MutableLiveData<Story?>()
    val storyDetail: LiveData<Story?> = _storyDetail

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun getStoryDetail(id: String) {
        _isLoading.value = true
        viewModelScope.launch {
            val response = repository.getStoryDetail(id)
            response.enqueue(object : Callback<StoryByIDResponse> {
                override fun onResponse(
                    call: Call<StoryByIDResponse>,
                    response: Response<StoryByIDResponse>
                ) {
                    _isLoading.value = false
                    if (response.isSuccessful) {
                        response.body()?.let {
                            _storyDetail.value = it.story
                        } ?: run {
                        }
                    } else {
                        response.errorBody()?.let {
//                            Log.e("DetailViewModel", it.string())
                        }
                    }
                }

                override fun onFailure(call: Call<StoryByIDResponse>, t: Throwable) {
                    _isLoading.value = false
                }
            })
        }
    }
}