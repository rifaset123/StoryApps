package com.example.storyapps.ui.addStory

import androidx.lifecycle.ViewModel
import com.example.storyapps.data.repository.StoryRepository
import com.example.storyapps.data.repository.UserRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

class AddStoryViewModel (private val repository: UserRepository, private val story : StoryRepository) : ViewModel(){
    suspend fun getToken(): String {
        return repository.getSession().map { it.token }.first()
    }
}