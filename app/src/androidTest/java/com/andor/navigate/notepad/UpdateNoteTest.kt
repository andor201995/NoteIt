package com.andor.navigate.notepad

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.*
import com.andor.navigate.notepad.base.BaseEspressoTest
import com.andor.navigate.notepad.listing.adapter.ListingAdapter
import org.hamcrest.Matchers
import org.junit.Test

class UpdateNoteTest : BaseEspressoTest() {

    @Test
    fun updateNoteTitleTest() {
        getUpdateFragment()

        val appCompatEditText2 = onView(
            Matchers.allOf(
                withId(R.id.newNoteHeadText),
                childAtPosition(
                    Matchers.allOf(
                        withId(R.id.addFragmentContainer),
                        childAtPosition(
                            withId(R.id.design_bottom_sheet),
                            0
                        )
                    ),
                    1
                ),
                isDisplayed()
            )
        )
        appCompatEditText2.perform(
            replaceText("test Updated"),
            closeSoftKeyboard()
        )

        closeSoftKeyboard()

        val appCompatImageButton2 = onView(
            Matchers.allOf(
                withId(R.id.oldNoteUpdateButton),
                withContentDescription(R.string.new_hading_accepted),
                isDisplayed()
            )
        )
        appCompatImageButton2.perform(click())

        onView(withText("test Updated")).check(matches(isDisplayed()))

    }

    @Test
    fun updateNoteEmptyTitle() {
        getUpdateFragment()

        val appCompatEditText2 = onView(
            Matchers.allOf(
                withId(R.id.newNoteHeadText),
                childAtPosition(
                    Matchers.allOf(
                        withId(R.id.addFragmentContainer),
                        childAtPosition(
                            withId(R.id.design_bottom_sheet),
                            0
                        )
                    ),
                    1
                ),
                isDisplayed()
            )
        )
        appCompatEditText2.perform(
            replaceText("  "),
            closeSoftKeyboard()
        )

        closeSoftKeyboard()

        val appCompatImageButton2 = onView(
            Matchers.allOf(
                withId(R.id.oldNoteUpdateButton),
                withContentDescription(R.string.new_hading_accepted),
                isDisplayed()
            )
        )
        appCompatImageButton2.perform(click())

        onView(withText(R.string.empty_title_toast)).inRoot(isToast()).check(matches(isDisplayed()))

    }

    private fun getUpdateFragment() {
        val selectFirstItemOfRecyclerView =
            onView(withId(R.id.listRecyclerView))


        selectFirstItemOfRecyclerView.perform(
            RecyclerViewActions.actionOnItemAtPosition<ListingAdapter.ListingHolder>(
                0,
                click()
            )
        )

        val actionMenuItemView = onView(
            Matchers.allOf(
                withId(R.id.edit),
                withContentDescription(R.string.edit),
                isDisplayed()
            )
        )
        actionMenuItemView.perform(longClick())
        actionMenuItemView.perform(doubleClick())
        pressBack()
        actionMenuItemView.perform(click())

        val appCompatEditText = onView(
            Matchers.allOf(
                withId(R.id.newNoteHeadText),
                childAtPosition(
                    Matchers.allOf(
                        withId(R.id.addFragmentContainer),
                        childAtPosition(
                            withId(R.id.design_bottom_sheet),
                            0
                        )
                    ),
                    1
                ),
                isDisplayed()
            )
        )
        appCompatEditText.perform(click())
    }
}