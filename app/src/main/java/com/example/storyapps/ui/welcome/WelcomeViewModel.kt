package com.example.storyapps.ui.welcome

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.storyapps.data.pref.UserModel
import com.example.storyapps.data.repository.UserRepository
import kotlinx.coroutines.launch

class WelcomeViewModel (private val repository: UserRepository) : ViewModel() {
    fun getSession(): LiveData<UserModel> {
        Log.d("WelcomeViewModel", "Checking if getSession works ${repository.getSession().asLiveData()}")
        return repository.getSession().asLiveData()
    }
}