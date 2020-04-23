package com.example.petland

import android.content.Intent
import android.view.Gravity
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.DrawerActions
import androidx.test.espresso.contrib.DrawerMatchers.isClosed
import androidx.test.espresso.contrib.NavigationViewActions
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry.getInstrumentation
import androidx.test.rule.ActivityTestRule
import com.example.petland.sign.BootActivity
import com.robotium.solo.Solo
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class LoginTest {
    private val testUser = "test"
    private val testPassword = "test"

    @get:Rule
    val activityRule = ActivityTestRule(BootActivity::class.java)

    private lateinit var solo: Solo

    @Before
    fun setUp() {
        solo = Solo(getInstrumentation(), activityRule.activity)
        solo.unlockScreen()
        activityRule.activity.sendBroadcast(Intent(Intent.ACTION_CLOSE_SYSTEM_DIALOGS))
    }

    @Test
    fun testUserCanLogin() {
        loginWithTestUser()
        onView(withId(R.id.drawer_layout)).check(matches(isDisplayed()))
    }

    @Test
    fun testUserCanLogOut() {
        loginWithTestUser()
        chooseItemFromNavbar(R.id.nav_logout)
        onView(withId(R.id.welcomeTitle)).check(matches(isDisplayed()))
    }

    private fun loginWithTestUser() {
        onView(withId(R.id.buttonSignIn)).perform(click())
        onView(withId(R.id.editTextUsername)).perform(typeText(testUser), closeSoftKeyboard())
        onView(withId(R.id.editTextPassword)).perform(typeText(testPassword), closeSoftKeyboard())
        onView(withId(R.id.buttonContinuar)).perform(click())
        Thread.sleep(500)
    }

    private fun chooseItemFromNavbar(id: Int) {
        onView(withId(R.id.drawer_layout))
            .check(matches(isClosed(Gravity.LEFT)))
            .perform(DrawerActions.open())

        onView(withId(R.id.nav_view))
            .perform(NavigationViewActions.navigateTo(id))

    }

}