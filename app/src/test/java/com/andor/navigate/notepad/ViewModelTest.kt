package com.andor.navigate.notepad

import com.andor.navigate.notepad.base.BaseUnitTest
import com.andor.navigate.notepad.base.observeOnce
import com.andor.navigate.notepad.core.AlertEvent
import com.andor.navigate.notepad.core.EventOnFragment
import com.andor.navigate.notepad.listing.dao.NoteModel
import org.junit.Before
import org.junit.Test


class ViewModelTest : BaseUnitTest() {

    @Before
    fun setUpStreams() {
        noteViewModel.actionAddNote(EventOnFragment.AddNoteEvent.AddNote(NoteModel()))
        noteViewModel.actionAddNote(EventOnFragment.AddNoteEvent.AddNote(NoteModel(head = "")))

    }

    @Test
    fun addingNoteWithEmptyTitle() {
        noteViewModel.actionAddNote(
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
        noteViewModel.actionAddNote(
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