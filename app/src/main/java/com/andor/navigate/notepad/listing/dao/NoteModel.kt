package com.andor.navigate.notepad.listing.dao

import com.google.firebase.firestore.Exclude

data class NoteModel(
    val head: String = DEFAULT_HEAD,

    val body: String = DEFAULT_BODY,

    val bg: String = NOTE_BG0,

    @set:Exclude
    @get:Exclude
    var id: String = DEFAULT_ID

) {
    companion object {
        const val DEFAULT_ID = "Default ID"
        const val DEFAULT_BODY = "Default Body"
        const val DEFAULT_HEAD = "Default Head"
        const val NOTE_BG0 = "BG0"
        const val NOTE_BG1 = "BG1"
        const val NOTE_BG2 = "BG2"
        const val NOTE_BG3 = "BG3"
        const val NOTE_BG4 = "BG4"
        const val NOTE_BG5 = "BG5"
    }

    constructor() : this("", "", "", "")


}