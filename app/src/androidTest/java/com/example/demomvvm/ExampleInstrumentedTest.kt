package com.example.demomvvm

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import androidx.test.platform.app.InstrumentationRegistry
import com.example.demomvvm.login.LoginActivity
import com.example.demomvvm.user.UserListActivity

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule


/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4ClassRunner::class)
class ExampleInstrumentedTest {

    @get:Rule
    var activityRule: ActivityScenarioRule<LoginActivity> =
        ActivityScenarioRule(LoginActivity::class.java)

    @get:Rule
    var activityRule1: ActivityScenarioRule<UserListActivity> =
        ActivityScenarioRule(UserListActivity::class.java)


    @Before
    fun setup() {
        Intents.init()
    }

    @Test
    fun checkPackageName() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("com.example.demomvvm", appContext.packageName)
    }

    @Test
    fun checkEditextWriteBle() {
        // Type text and then press the button.
        // Check that the text was changed.
        onView(withId(R.id.editEmail))
            .check(matches(withText("eve.holt@reqres.in")));
    }

    @Test
    fun launchActivityTest() {

        onView(ViewMatchers.withId(R.id.btnLogin))
            .perform(click())

        intended(hasComponent(UserListActivity::class.java.name))
    }

    @Test
    fun checkTargetVersion() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals(29, appContext.applicationInfo.targetSdkVersion)
    }

    @Test
    fun checkMinVersion() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals(17, appContext.applicationInfo.minSdkVersion)
    }


}
