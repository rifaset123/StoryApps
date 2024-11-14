package com.example.storyapps.ui.login

import android.app.AlertDialog
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.storyapps.data.pref.UserModel
import com.example.storyapps.data.repository.UserRepository
import kotlinx.coroutines.launch

class LoginViewModel(private val repository: UserRepository) : ViewModel() {

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _loggedInUser = MutableLiveData<UserModel?>()
    val loggedInUser: LiveData<UserModel?> = _loggedInUser

    private val _isLogin = MutableLiveData<Boolean>()
    val isLogin: LiveData<Boolean> = _isLogin

    private var extraToken: String? = null

    fun saveSession(user: UserModel) {
        viewModelScope.launch {
            repository.saveSession(user)
        }
    }

    fun login(email: String, password: String) {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val result = repository.login(email, password)
                if (result.error == false) {
                    _isLogin.value = true
                    val loginResult = result.loginResult
                    if (loginResult != null) {
                        val user = loginResult.token?.let { UserModel(email, it, true) }
                        _loggedInUser.postValue(user)
                        extraToken = user?.token
                        Log.d("LoginViewModel", "login: $user")
                        Log.d("LoginViewModel", "login: ${user?.token}")
                        if (user != null) {
                            saveSession(user)
                        }
                    }
                } else {
                    // Handle bad request
                    Log.e("LoginViewModel1", "Login failed: ${result.message}")
                    _isLogin.value = false
                }
            } catch (e: Exception) {
                // Handle exception
                Log.e("LoginViewModel1", "Login failed with exception: ${e.message}")
                _isLogin.value = false
                Log.e("LoginViewModel1", "Login failed: $isLogin")
            } finally {
                _isLoading.value = false
            }
        }
    }
}