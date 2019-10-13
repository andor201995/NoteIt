package com.andor.navigate.notepad

import android.view.View
import android.view.ViewGroup
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import androidx.test.runner.AndroidJUnit4
import com.andor.navigate.notepad.auth.AuthActivity
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.Matchers
import org.hamcrest.TypeSafeMatcher
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4::class)
class ListTypeTest {

    @Rule
    @JvmField
    var mActivityTestRule = ActivityTestRule(AuthActivity::class.java)

    @Test
    fun selectGridListAdapter() {
        val settingButtonAction = Espresso.onView(
            Matchers.allOf(
                ViewMatchers.withId(R.id.action_setting),
                ViewMatchers.withContentDescription(R.string.action_settings),
                ViewMatchers.isDisplayed()
            )
        )
        settingButtonAction.perform(ViewActions.click())

        val selectGridAction = Espresso.onView(
            Matchers.allOf(
                ViewMatchers.withId(R.id.setting_view_type_grid),
                childAtPosition(ViewMatchers.withId(R.id.listTypeLayout), 1),
                ViewMatchers.isDisplayed()
            )
        )
        selectGridAction.perform(ViewActions.click())
    }

    @Test
    fun selectStaggeredListAdapter() {
        val settingButtonAction = Espresso.onView(
            Matchers.allOf(
                ViewMatchers.withId(R.id.action_setting),
                ViewMatchers.withContentDescription(R.string.action_settings),
                ViewMatchers.isDisplayed()
            )
        )
        settingButtonAction.perform(ViewActions.click())

        val selectGridAction = Espresso.onView(
            Matchers.allOf(
                ViewMatchers.withId(R.id.setting_view_type_staggered),
                childAtPosition(ViewMatchers.withId(R.id.listTypeLayout), 2),
                ViewMatchers.isDisplayed()
            )
        )
        selectGridAction.perform(ViewActions.click())
    }


    private fun childAtPosition(
        parentMatcher: Matcher<View>, position: Int
    ): Matcher<View> {

        return object : TypeSafeMatcher<View>() {
            override fun describeTo(description: Description) {
                description.appendText("Child at position $position in parent ")
                parentMatcher.describeTo(description)
            }

            public override fun matchesSafely(view: View): Boolean {
                val parent = view.parent
                return parent is ViewGroup && parentMatcher.matches(parent)
                        && view == parent.getChildAt(position)
            }
        }
    }
}

