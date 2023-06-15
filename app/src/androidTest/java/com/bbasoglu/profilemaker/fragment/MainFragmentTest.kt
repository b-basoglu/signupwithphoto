package com.bbasoglu.profilemaker.fragment

import android.accessibilityservice.AccessibilityService
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.intent.Intents
import androidx.test.platform.app.InstrumentationRegistry
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.RuleChain
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.internal.runner.junit4.statement.UiThreadStatement.runOnUiThread
import com.bbasoglu.profilemaker.ui.MainActivity
import com.bbasoglu.profilemaker.R
import androidx.navigation.findNavController

@HiltAndroidTest
class MainFragmentTest {
    private val hiltRule = HiltAndroidRule(this)
    private val activityTestRule = ActivityScenarioRule(MainActivity::class.java)
    @get:Rule
    val rule = RuleChain
        .outerRule(hiltRule)
        .around(activityTestRule)

    @Before
    fun jumpToMainFragment() {
        activityTestRule.apply {
            runOnUiThread {
                this.scenario.onActivity {
                    it.findNavController(R.id.container).navigate(R.id.main_nav)
                }
            }
        }
    }
    @Test
    fun testEmptyClickAndBack() {
        Intents.init()

        Espresso.onView(withId(com.bbasoglu.signup.R.id.continueBtn))
            .perform(ViewActions.click())
        Espresso.onView(withId(com.bbasoglu.signup.R.id.snackbarAction))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))

        InstrumentationRegistry.getInstrumentation()
            .uiAutomation
            .performGlobalAction(AccessibilityService.GLOBAL_ACTION_BACK)

    }

}