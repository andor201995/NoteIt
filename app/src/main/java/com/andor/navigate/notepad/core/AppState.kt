package com.andor.navigate.notepad.core

import com.andor.navigate.notepad.listing.dao.NoteModel

data class AppState(
    val selectedNote: NoteModel? = null,
    val listingType: ListingType = ListingType.Linear,
    val listOfAllNotes: ArrayList<NoteModel> = ArrayList(),
    val currentUserID: String,
    val sortingType: SortingType = SortingType.Alphabet
)


sealed class ListingType {
    object Linear : ListingType()
    object Grid : ListingType()
    object Staggered : ListingType()
}

sealed class SortingType {
    object Alphabet : SortingType()
    object DateCreated : SortingType()
    object DateUpdated : SortingType()
}

sealed class EventOnFragment {
    object None : EventOnFragment()

    sealed class AddNoteEvent : EventOnFragment() {
        data class AddNote(val noteModel: NoteModel) : AddNoteEvent()
        data class UpdateNote(val noteModel: NoteModel) : AddNoteEvent()
        object Cancel : AddNoteEvent()
    }

    sealed class UpdateNoteEvent : EventOnFragment() {
        data class FragmentStop(val newBody: String) : UpdateNoteEvent()
        data class TextChanged(val newBody: String) : UpdateNoteEvent()
    }

    sealed class ExpandedNoteEvent : EventOnFragment() {
        object OpenUpdateNoteFragment : ExpandedNoteEvent()
        object OpenEditNoteBottomSheet : ExpandedNoteEvent()
    }

    sealed class ListNoteEvent : EventOnFragment()

    sealed class SettingNoteEvent : EventOnFragment()
}

sealed class AlertEvent {
    object TitleEmptyToast : AlertEvent()
    object None : AlertEvent()
}