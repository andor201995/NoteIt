package com.andor.navigate.notepad

import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.matcher.ViewMatchers
import com.andor.navigate.notepad.base.BaseEspressoTest
import org.hamcrest.Matchers
import org.junit.Test

class ListTypeTest : BaseEspressoTest() {

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
}

