package com.andor.navigate.notepad

import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.matcher.ViewMatchers
import com.andor.navigate.notepad.base.BaseEspressoTest
import org.hamcrest.Matchers
import org.junit.Test

class SortingTypeTest : BaseEspressoTest() {

    @Test
    fun selectSortAlphabeticOrder() {
        val settingButtonAction = Espresso.onView(
            Matchers.allOf(
                ViewMatchers.withId(R.id.action_setting),
                ViewMatchers.withContentDescription(R.string.action_settings),
                ViewMatchers.isDisplayed()
            )
        )
        settingButtonAction.perform(ViewActions.click())

        val selectSortAction = Espresso.onView(
            Matchers.allOf(
                ViewMatchers.withId(R.id.sortByAlphabet),
                childAtPosition(ViewMatchers.withId(R.id.sortTypeLayout), 0),
                ViewMatchers.isDisplayed()
            )
        )
        selectSortAction.perform(ViewActions.click())
    }

    @Test
    fun selectSortDateCreatedOrder() {
        val settingButtonAction = Espresso.onView(
            Matchers.allOf(
                ViewMatchers.withId(R.id.action_setting),
                ViewMatchers.withContentDescription(R.string.action_settings),
                ViewMatchers.isDisplayed()
            )
        )
        settingButtonAction.perform(ViewActions.click())

        val selectSortAction = Espresso.onView(
            Matchers.allOf(
                ViewMatchers.withId(R.id.sortByCreated),
                childAtPosition(ViewMatchers.withId(R.id.sortTypeLayout), 2),
                ViewMatchers.isDisplayed()
            )
        )
        selectSortAction.perform(ViewActions.click())
    }

    @Test
    fun selectSortDateUpdatedOrder() {
        val settingButtonAction = Espresso.onView(
            Matchers.allOf(
                ViewMatchers.withId(R.id.action_setting),
                ViewMatchers.withContentDescription(R.string.action_settings),
                ViewMatchers.isDisplayed()
            )
        )
        settingButtonAction.perform(ViewActions.click())

        val selectSortAction = Espresso.onView(
            Matchers.allOf(
                ViewMatchers.withId(R.id.sortByRecentChange),
                childAtPosition(ViewMatchers.withId(R.id.sortTypeLayout), 1),
                ViewMatchers.isDisplayed()
            )
        )
        selectSortAction.perform(ViewActions.click())
    }

}