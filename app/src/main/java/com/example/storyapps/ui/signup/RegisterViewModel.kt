package com.example.storyapps.ui.signup

import android.text.Editable
import android.util.Log
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

    fun register(name: String, email: String, password: String) {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val result = repository.register(name, email, password)
                if (result.error == false){
                    _isRegister.value = true
                    _registerStatus.postValue(result)
                    Log.d("RegisterViewModel", "register: $result, with : $name, $email, $password")
                    _isLoading.value = false
                } else {
                    Log.e("RegisterViewModel", "register: ${result.message}")
                    _isLoading.value = false
                }
            } catch (e: Exception) {
                Log.e("RegisterViewModel", "register: ${e.message}")
                _isRegister.value = false
            } finally {
                _isLoading.value = false
            }
        }
    }
}