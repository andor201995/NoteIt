package com.andor.navigate.notepad.core

import com.andor.navigate.notepad.listing.dao.NoteModel

data class AppState(
    val selectedNote: NoteModel? = null,
    val listingType: ListingType = ListingType.Linear,
    val listOfAllNotes: ArrayList<NoteModel> = ArrayList(),
    val currentUserID: String,
    val bottomMenuEvent: Event<BottomMenuEvent> = Event(BottomMenuEvent.Close),
    val bottomMenuType: BottomMenuType = BottomMenuType.None
)


sealed class ListingType {
    object Linear : ListingType()
    object Grid : ListingType()
    object Staggered : ListingType()
}

sealed class BottomMenuEvent {
    object Open : BottomMenuEvent()
    object AddNote : BottomMenuEvent()
    object Close : BottomMenuEvent()
}

sealed class BottomMenuType {
    object None : BottomMenuType()
    object Setting : BottomMenuType()
    object AddNote : BottomMenuType()
}