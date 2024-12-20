package com.example.storyapps.data.retrofit

import com.example.storyapps.data.response.FileUploadResponse
import com.example.storyapps.data.response.LoginResponse
import com.example.storyapps.data.response.RegisterResponse
import com.example.storyapps.data.response.StoryByIDResponse
import com.example.storyapps.data.response.StoryResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path

interface ApiService {

//    @GET("events?active=1")
//    fun getEvents(): Call<EventResponse>

    // register
    @FormUrlEncoded
    @POST("register")
    suspend fun register(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String
    ): RegisterResponse

    // login
    @FormUrlEncoded
    @POST("login")
    suspend fun login(
        @Field("email") email: String,
        @Field("password") password: String
    ): LoginResponse

    // getter
    @GET("stories")
    suspend fun getStories(
    ): StoryResponse

    // get story by id
    @GET("stories/{id}")
    fun getStoryDetail(@Path("id") storyId: String): Call<StoryByIDResponse>

    // add story
    @Multipart
    @POST("stories")
    suspend fun uploadImage(
        @Part file: MultipartBody.Part,
        @Part ("description") description: RequestBody,
    ) : FileUploadResponse
}