package com.andor.navigate.notepad


import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.longClick
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.*
import com.andor.navigate.notepad.base.BaseEspressoTest
import com.andor.navigate.notepad.listing.adapter.ListingAdapter
import org.hamcrest.Matchers.allOf
import org.junit.Test

class DeleteNoteTest : BaseEspressoTest() {

    @Test
    fun deleteNote() {
        val selectFirstItemOfRecyclerView = onView(withId(R.id.listRecyclerView))

        selectFirstItemOfRecyclerView.perform(
            RecyclerViewActions.actionOnItemAtPosition<ListingAdapter.ListingHolder>(
                0,
                longClick()
            )
        )

        val actionMenuItemView = onView(
            allOf(
                withId(R.id.SelectedItemDelete), withContentDescription(R.string.delete),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.action_mode_bar),
                        1
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        actionMenuItemView.perform(click())
    }
}
