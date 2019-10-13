package com.andor.navigate.notepad


import android.view.View
import android.view.ViewGroup
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import androidx.test.runner.AndroidJUnit4
import com.andor.navigate.notepad.auth.AuthActivity
import com.andor.navigate.notepad.listing.adapter.ListingAdapter
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.Matchers.allOf
import org.hamcrest.TypeSafeMatcher
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4::class)
class SampleNoteTest {

    @Rule
    @JvmField
    var mActivityTestRule = ActivityTestRule(AuthActivity::class.java)

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

    @Test
    fun selectGridListAdapter() {
        val settingButtonAction = onView(
            allOf(
                withId(R.id.action_setting),
                withContentDescription(R.string.action_settings),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.toolbar),
                        1
                    ),
                    1
                ),
                isDisplayed()
            )
        )
        settingButtonAction.perform(click())

        val selectGridAction = onView(
            allOf(
                withId(R.id.setting_view_type_grid),
                childAtPosition(withId(R.id.listTypeLayout), 1),
                isDisplayed()
            )
        )
        selectGridAction.perform(click())
        Thread.sleep(2000)
    }

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
