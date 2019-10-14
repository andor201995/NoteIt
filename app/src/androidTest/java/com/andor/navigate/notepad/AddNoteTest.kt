package com.andor.navigate.notepad


import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import com.andor.navigate.notepad.base.BaseEspressoTest
import org.hamcrest.Matchers.allOf
import org.junit.Test


class AddNoteTest : BaseEspressoTest() {

    /*
    * recorder test for reference of creating a Note
    * */
    @Test
    fun addSampleNoteTest() {

        val actionMenuItemView = onView(
            allOf(
                withId(R.id.action_add), withContentDescription(R.string.add_note),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.toolbar),
                        1
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        actionMenuItemView.perform(click())

        val appCompatEditText = onView(
            allOf(
                withId(R.id.newNoteHeadText),
                childAtPosition(
                    allOf(
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

        val appCompatEditText2 = onView(
            allOf(
                withId(R.id.newNoteHeadText),
                childAtPosition(
                    allOf(
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
        appCompatEditText2.perform(replaceText("test"), closeSoftKeyboard())

        closeSoftKeyboard()

        val appCompatImageButton = onView(
            allOf(
                withId(R.id.btn_bg_3),
                childAtPosition(
                    allOf(
                        withId(R.id.linearLayoutCompat),
                        childAtPosition(
                            withId(R.id.addFragmentContainer),
                            2
                        )
                    ),
                    3
                ),
                isDisplayed()
            )
        )
        appCompatImageButton.perform(click())

        val appCompatImageButton2 = onView(
            allOf(
                withId(R.id.newNoteButtonAccept),
                withContentDescription(R.string.new_hading_accepted),
                childAtPosition(
                    allOf(
                        withId(R.id.addFragmentContainer),
                        childAtPosition(
                            withId(R.id.design_bottom_sheet),
                            0
                        )
                    ),
                    3
                ),
                isDisplayed()
            )
        )
        appCompatImageButton2.perform(click())

        val appCompatEditText3 = onView(
            allOf(
                withId(R.id.newNoteBodyTxt),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.nav_host),
                        0
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        appCompatEditText3.perform(click())

        val appCompatEditText4 = onView(
            allOf(
                withId(R.id.newNoteBodyTxt),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.nav_host),
                        0
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        appCompatEditText4.perform(replaceText("test"), closeSoftKeyboard())

    }

    @Test
    fun addSampleNoteEmptyTitleTest() {

        val actionMenuItemView = onView(
            allOf(
                withId(R.id.action_add), withContentDescription(R.string.add_note),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.toolbar),
                        1
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        actionMenuItemView.perform(click())

        val appCompatEditText = onView(
            allOf(
                withId(R.id.newNoteHeadText),
                childAtPosition(
                    allOf(
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

        val appCompatEditText2 = onView(
            allOf(
                withId(R.id.newNoteHeadText),
                childAtPosition(
                    allOf(
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
        appCompatEditText2.perform(replaceText(""), closeSoftKeyboard())

        closeSoftKeyboard()

        val appCompatImageButton = onView(
            allOf(
                withId(R.id.btn_bg_3),
                childAtPosition(
                    allOf(
                        withId(R.id.linearLayoutCompat),
                        childAtPosition(
                            withId(R.id.addFragmentContainer),
                            2
                        )
                    ),
                    3
                ),
                isDisplayed()
            )
        )
        appCompatImageButton.perform(click())

        val appCompatImageButton2 = onView(
            allOf(
                withId(R.id.newNoteButtonAccept),
                withContentDescription(R.string.new_hading_accepted),
                childAtPosition(
                    allOf(
                        withId(R.id.addFragmentContainer),
                        childAtPosition(
                            withId(R.id.design_bottom_sheet),
                            0
                        )
                    ),
                    3
                ),
                isDisplayed()
            )
        )
        appCompatImageButton2.perform(click())

        onView(withText(R.string.empty_title_toast)).inRoot(isToast()).check(matches(isDisplayed()))

    }

    @Test
    fun cancelAddingNote() {
        val addNoteButtonAction = onView(
            allOf(
                withId(R.id.action_add),
                withContentDescription(R.string.add_note),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.toolbar),
                        1
                    ),
                    0
                ),
                isDisplayed()

            )
        )
        addNoteButtonAction.perform(longClick())
        addNoteButtonAction.perform(click())

        closeSoftKeyboard()

        val cancelAddNoteAction = onView(
            allOf(
                withId(R.id.newNoteButtonCancel),
                childAtPosition(
                    allOf(
                        withId(R.id.addFragmentContainer),
                        childAtPosition(
                            withId(R.id.design_bottom_sheet),
                            0
                        )
                    ),
                    5
                ),
                isDisplayed()
            )
        )

        cancelAddNoteAction.perform(click())
    }
}
