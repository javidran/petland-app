package com.example.petland

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class LoginTest {

    @get:Rule
    val activityRule = ActivityTestRule(MainActivity::class.java)

    @Test
    fun testUserCanLogin() {
        loginWithTestUser()
        onView(withId(R.id.buttonLogOut)).check(matches(isDisplayed()))
    }

    @Test
    fun testUserCanLogOut() {
        loginWithTestUser()
        onView(withId(R.id.buttonLogOut)).perform(click())
        onView(withId(R.id.welcomeTitle)).check(matches(isDisplayed()))
    }

    private fun loginWithTestUser() {
        onView(withId(R.id.buttonSignIn)).perform(click())
        onView(withId(R.id.editTextUsername)).perform(typeText("test"))
        onView(withId(R.id.editTextPassword)).perform(typeText("test"))
        onView(withId(R.id.buttonContinuar)).perform(click())
        Thread.sleep(500)
    }

}