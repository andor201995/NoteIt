package com.andor.navigate.notepad

import android.content.Context
import android.view.inputmethod.InputMethodManager
import androidx.test.espresso.Espresso.*
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.doesNotExist
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.platform.app.InstrumentationRegistry.getInstrumentation
import com.andor.navigate.notepad.base.BaseEspressoTest
import org.hamcrest.Matchers
import org.junit.Test

class SearchNoteTest : BaseEspressoTest() {

    @Test
    fun testSearchHead() {
        val searchButtonAction = onView(
            Matchers.allOf(
                withId(R.id.action_search),
                withContentDescription(R.string.search_hint),
                isDisplayed()
            )
        )
        searchButtonAction.perform(click())

        onView(withHint(R.string.search_hint)).check(matches(isDisplayed()))

        val settingButtonAction = onView(
            Matchers.allOf(
                withId(R.id.action_setting),
                withContentDescription(R.string.action_settings)
            )
        )
        settingButtonAction.check(doesNotExist())

        val addButtonAction = onView(
            Matchers.allOf(
                withId(R.id.action_add),
                withContentDescription(R.string.add_note)
            )
        )
        addButtonAction.check(doesNotExist())

        openActionBarOverflowOrOptionsMenu(getInstrumentation().targetContext)

        Thread.sleep(2000)

        val settingButtonAction1 = onView(withText(R.string.add_note))

        val addButtonAction1 = onView(withText(R.string.action_settings))

        closeSoftKeyboard()

        settingButtonAction1.check(matches(isDisplayed()))
        addButtonAction1.check(matches(isDisplayed()))


    }

    fun isKeyboardShown(): Boolean {
        val inputMethodManager =
            InstrumentationRegistry.getInstrumentation().targetContext.getSystemService(
                Context.INPUT_METHOD_SERVICE
            ) as InputMethodManager
        return inputMethodManager.isAcceptingText
    }

}