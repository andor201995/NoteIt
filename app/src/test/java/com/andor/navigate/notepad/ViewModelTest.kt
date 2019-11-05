package com.andor.navigate.notepad

import com.andor.navigate.notepad.base.BaseUnitTest
import com.andor.navigate.notepad.base.observeOnce
import com.andor.navigate.notepad.core.AlertEvent
import com.andor.navigate.notepad.core.EventOnFragment
import com.andor.navigate.notepad.listing.dao.NoteModel
import org.junit.Test


class ViewModelTest : BaseUnitTest() {

    @Test
    fun addingNoteWithEmptyTitle() {
        noteViewModel.handleFragmentEvent(
            EventOnFragment.AddNoteEvent.AddNote(NoteModel(head = ""))
        )
        noteViewModel.getAppEventStream().observeOnce { event ->
            val eventOnFragment = event.getContentIfNotHandled()
            assert(eventOnFragment is EventOnFragment.None)
        }

        noteViewModel.getAppAlertStream().observeOnce {
            val alertEvent = it.getContentIfNotHandled()
            assert(alertEvent is AlertEvent.TitleEmptyToast)
        }
    }

    @Test
    fun updateNoteWithEmptyTitle() {
        noteViewModel.handleFragmentEvent(
            EventOnFragment.AddNoteEvent.UpdateNote(NoteModel(head = ""))
        )
        noteViewModel.getAppEventStream().observeOnce { event ->
            val eventOnFragment = event.getContentIfNotHandled()
            assert(eventOnFragment is EventOnFragment.None)
        }

        noteViewModel.getAppAlertStream().observeOnce {
            val alertEvent = it.getContentIfNotHandled()
            assert(alertEvent is AlertEvent.TitleEmptyToast)
        }
    }

}