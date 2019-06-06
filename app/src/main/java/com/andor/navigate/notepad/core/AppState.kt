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
    object Stagered : ListingType()
}