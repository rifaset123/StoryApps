package com.example.storyapps.helper

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.storyapps.data.repository.StoryIDRepository
import com.example.storyapps.data.repository.StoryRepository
import com.example.storyapps.data.repository.UserRepository
import com.example.storyapps.di.Injection
import com.example.storyapps.ui.addStory.AddStoryViewModel
import com.example.storyapps.ui.detail.DetailViewModel
import com.example.storyapps.ui.login.LoginViewModel
import com.example.storyapps.ui.main.MainViewModel
import com.example.storyapps.ui.maps.MapsViewModel
import com.example.storyapps.ui.signup.RegisterViewModel
import com.example.storyapps.ui.welcome.WelcomeViewModel

class ViewModelFactory(
    private val repository: UserRepository,
    private val provideRepositoryStory: StoryRepository,
    private val provideRepositoryStoryID: StoryIDRepository
) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(MapsViewModel::class.java) -> {
                MapsViewModel(repository, provideRepositoryStory) as T
            }
            modelClass.isAssignableFrom(AddStoryViewModel::class.java) -> {
                AddStoryViewModel(repository, provideRepositoryStory) as T
            }
            modelClass.isAssignableFrom(DetailViewModel::class.java) -> {
                DetailViewModel(provideRepositoryStoryID) as T
            }
            modelClass.isAssignableFrom(WelcomeViewModel::class.java) -> {
                WelcomeViewModel(repository) as T
            }
            modelClass.isAssignableFrom(LoginViewModel::class.java) -> {
                LoginViewModel(repository) as T
            }
            modelClass.isAssignableFrom(RegisterViewModel::class.java) -> {
                RegisterViewModel(repository) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: ViewModelFactory? = null
        @JvmStatic
        fun getInstance(context: Context): ViewModelFactory {
            if (INSTANCE == null) {
                synchronized(ViewModelFactory::class.java) {
                    INSTANCE = ViewModelFactory(Injection.provideRepository(context), Injection.provideRepositoryStory(context), Injection.provideRepositoryStoryID(context))
                }
            }
            return INSTANCE as ViewModelFactory
        }
    }
}