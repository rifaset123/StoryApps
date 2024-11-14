package com.example.storyapps.data.repository

import com.example.storyapps.data.response.RegisterResponse
import com.example.storyapps.data.retrofit.ApiService

class LoginRepository (private val apiService: ApiService) {
    suspend fun register(name: String, email: String, password: String): RegisterResponse {
        return apiService.register(name, email, password)
    }
}