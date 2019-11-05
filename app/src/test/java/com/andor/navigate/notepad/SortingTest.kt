package com.andor.navigate.notepad

import com.andor.navigate.notepad.base.BaseUnitTest
import com.andor.navigate.notepad.core.SortingType
import org.junit.Test

class SortingTest : BaseUnitTest() {

    @Test
    fun sortingAlphabetically() {
        noteViewModel.changeSortingType(SortingType.Alphabet)
        assert(noteViewModel.getAppStateStream().value!!.sortingType == SortingType.Alphabet)
    }

    @Test
    fun sortingDateCreated() {
        noteViewModel.changeSortingType(SortingType.DateCreated)
        assert(noteViewModel.getAppStateStream().value!!.sortingType == SortingType.DateCreated)
    }

    @Test
    fun sortingDateupdated() {
        noteViewModel.changeSortingType(SortingType.DateUpdated)
        assert(noteViewModel.getAppStateStream().value!!.sortingType == SortingType.DateUpdated)
    }
}