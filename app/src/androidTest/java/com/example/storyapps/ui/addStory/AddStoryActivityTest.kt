package com.example.storyapps.ui.addStory

import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.GrantPermissionRule
import com.example.storyapps.R
import com.example.storyapps.helper.EspressoIdlingResource
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class AddStoryActivityTest {

    @get:Rule
    val permissionRule: GrantPermissionRule =
        GrantPermissionRule.grant(android.Manifest.permission.CAMERA)

    @Test
    fun testAddStory() {
        Thread.sleep(6000)
        ActivityScenario.launch(AddStoryActivity::class.java)

        // open galery and click first image
        onView(withId(R.id.galleryButton)).perform(click())
        Thread.sleep(2000) // wait for gallery to open
        onView(withId(R.id.galleryButton)).perform(click()) // assuming the first image has an id 'gallery'

        // Isi deskripsi
        onView(withId(R.id.TextAreaDescription)).perform(typeText("tess"))

        // Klik tombol upload
        onView(withId(R.id.uploadButton)).perform(click())

        // Tunggu hingga proses selesai
        EspressoIdlingResource.increment()
        onView(withText(R.string.upload_success)).check(matches(isDisplayed()))
        EspressoIdlingResource.decrement()
    }
}
