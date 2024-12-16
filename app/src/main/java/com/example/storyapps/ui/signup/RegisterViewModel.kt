package com.example.storyapps.ui.signup

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.storyapps.data.pref.UserModel
import com.example.storyapps.data.repository.UserRepository
import com.example.storyapps.data.response.RegisterResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class RegisterViewModel(private val repository: UserRepository) : ViewModel() {
    private val coroutineScope: CoroutineScope = CoroutineScope(Dispatchers.IO)

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _registerStatus = MutableLiveData<RegisterResponse>()
    val registerStatus: LiveData<RegisterResponse> = _registerStatus

    private val _isRegister = MutableLiveData<Boolean>()
    val isRegister: LiveData<Boolean> = _isRegister


    private val _logoutStatus = MutableLiveData<Boolean>()
    val logoutStatus: LiveData<Boolean> = _logoutStatus

    fun saveSession(user: UserModel) {
        viewModelScope.launch {
            repository.saveSession(user)
        }
    }

    fun isPasswordValid(password: String): Boolean {
        return password.length >= 8
    }

    fun isNameValid(name: String): Boolean {
        return name.isNotEmpty()
    }

    fun isEmailValid(email: String): Boolean {
        val emailPattern = java.util.regex.Pattern.compile(
            "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$"
        )
        return emailPattern.matcher(email).matches()
    }

    fun isFormValid(name: String, email: String, password: String): Boolean {
        return isNameValid(name) && isEmailValid(email) && isPasswordValid(password)
    }

    fun register(name: String, email: String, password: String) {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val result = repository.register(name, email, password)
                if (result.error == false){
                    _isRegister.value = true
                    _registerStatus.postValue(result)
                    _isLoading.value = false
                } else {
                    _isLoading.value = false
                }
            } catch (e: Exception) {
                _isRegister.value = false
            } finally {
                _isLoading.value = false
            }
        }
    }
}