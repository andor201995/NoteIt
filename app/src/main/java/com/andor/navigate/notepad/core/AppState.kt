package com.andor.navigate.notepad.core

import com.andor.navigate.notepad.listing.dao.NoteModel

data class AppState(
    val selectedNote: NoteModel? = null,
    val listingType: ListingType = ListingType.Linear,
    val listOfAllNotes: ArrayList<NoteModel> = ArrayList(),
    val currentUserID: String
)


sealed class ListingType {
    object Linear : ListingType()
    object Grid : ListingType()
    object Staggered : ListingType()
}

sealed class EventOnFragment {
    object None : EventOnFragment()
    sealed class AddNoteEvent : EventOnFragment() {
        data class AddNote(val noteModel: NoteModel) : AddNoteEvent()
        data class UpdateNote(val noteModel: NoteModel) : AddNoteEvent()
        object Cancel : AddNoteEvent()
    }
}

sealed class AlertEvent {
    object TitleEmptyToast : AlertEvent()
    object None : AlertEvent()
}