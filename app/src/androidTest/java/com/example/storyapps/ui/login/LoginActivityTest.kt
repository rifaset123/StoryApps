package com.example.storyapps.ui.login

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.closeSoftKeyboard
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isAssignableFrom
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.storyapps.MainDispatcherRule
import com.example.storyapps.R
import com.example.storyapps.ui.customView.EmailEditText
import com.example.storyapps.ui.customView.PasswordEditText
import com.example.storyapps.ui.welcome.WelcomeActivity
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
class LoginActivityTest {
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainDispatcherRules = MainDispatcherRule()

    // NOTE : tes ini harusnya work cuman gatau kenapa gamau perform type, keknya karna customView nya gak inherit EditText (?)
    @Test
    fun testLoginLogout() {
        Thread.sleep(6000)
        ActivityScenario.launch(WelcomeActivity::class.java)

        onView(withId(R.id.loginButton)).perform(click())

        // Input email and password
        Thread.sleep(6000)
        onView(withId(R.id.emailEditTextLayoutLogin)).perform(click())
        onView(isAssignableFrom(EmailEditText::class.java)).perform(typeText("rifaset123@gmail.com"), closeSoftKeyboard())
        onView(withId(R.id.passwordEditTextLayoutLogin)).perform(click())
        onView(isAssignableFrom(PasswordEditText::class.java)).perform(typeText("rifaset123"), closeSoftKeyboard())

        // Click login button
        onView(withId(R.id.loginButton)).perform(click())

        // Verify login success
        onView(withText(R.string.you_have_successfully_logged_in_enjoy_all_the_features))
            .check(matches(isDisplayed()))

        // Click logout button (assuming it is in MainActivity)
        onView(withId(R.id.action_logout)).perform(click())
    }
}