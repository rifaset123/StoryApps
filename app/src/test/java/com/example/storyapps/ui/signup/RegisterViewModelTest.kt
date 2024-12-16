package com.example.storyapps.ui.signup

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.storyapps.MainDispatcherRule
import com.example.storyapps.data.repository.StoryRepository
import com.example.storyapps.data.repository.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations

@ExperimentalCoroutinesApi
class RegisterViewModelTest {
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainDispatcherRules = MainDispatcherRule() // menjalankan coroutin memanfaatkan TestDispatcher

    @Mock
    private lateinit var repository: StoryRepository

    @Mock
    private lateinit var userRepository: UserRepository

    private lateinit var viewModel: RegisterViewModel

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        viewModel = RegisterViewModel(userRepository)
    }


    @Test
    fun `Register should call repository`(): Unit = runBlocking {
        val name = "rifa"
        val email = "rifaset123@gmail.com"
        val password = "rifaset123"
        viewModel.register(name, email, password)
        verify(userRepository).register(name, email, password)
    }

    @Test
    fun `must failed  when password must less 8 `() {
        val password = "1234567"
        val result = viewModel.isPasswordValid(password)
        assertFalse(result)
    }

    @Test
    fun `must success when password more than 8 `() {
        val password = "rifaset123"
        val result = viewModel.isPasswordValid(password)
        assert(result)
    }

    @Test
    fun `name must not empty`() {
        val uName = "Rifa Indra Setiawan"
        val res = viewModel.isNameValid(uName)
        assert(res) { "Name must not empty done " }
    }

    @Test
    fun `email must be valid`(){
        val email = "rifaset123@gmail.com"
        val res = viewModel.isEmailValid(email)
        assert(res)
    }

    @Test
    fun `form must valid with input from name password email`(){
        val name = "rifaset123"
        val email = "rifaset123@gmail.com"
        val password = "rifaset123"
        val res = viewModel.isFormValid(name, email, password)
        assert(res)
    }
}