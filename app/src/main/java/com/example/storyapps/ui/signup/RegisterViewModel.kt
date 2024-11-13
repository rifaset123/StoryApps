package com.example.storyapps.ui.signup

import android.text.Editable
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.storyapps.data.pref.UserModel
import com.example.storyapps.data.repository.UserRepository
import kotlinx.coroutines.launch

class RegisterViewModel(private val repository: UserRepository) : ViewModel() {
    private val _email = MutableLiveData<String>()
    val email: LiveData<String> get() = _email

    private val _password = MutableLiveData<String>()
    val password: LiveData<String> get() = _password


    fun saveSession(user: UserModel) {
        viewModelScope.launch {
            repository.saveSession(user)
        }
    }
}