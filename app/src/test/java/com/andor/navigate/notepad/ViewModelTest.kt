package com.andor.navigate.notepad

import com.andor.navigate.notepad.base.BaseUnitTest
import com.andor.navigate.notepad.base.observeOnce
import com.andor.navigate.notepad.core.AlertEvent
import com.andor.navigate.notepad.core.EventOnFragment
import com.andor.navigate.notepad.listing.dao.NoteModel
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class ViewModelTest : BaseUnitTest() {

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