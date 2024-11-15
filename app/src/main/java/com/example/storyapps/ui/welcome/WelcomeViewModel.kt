package com.example.storyapps.ui.welcome

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.storyapps.data.pref.UserModel
import com.example.storyapps.data.repository.UserRepository

class WelcomeViewModel (private val repository: UserRepository) : ViewModel() {
    fun getSession(): LiveData<UserModel> {
        return repository.getSession().asLiveData()
    }
}