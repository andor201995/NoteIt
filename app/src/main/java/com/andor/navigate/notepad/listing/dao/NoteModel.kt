package com.andor.navigate.notepad.listing.dao

import com.google.firebase.firestore.Exclude

data class NoteModel(
    val head: String = DEFAULT_HEAD,

    val body: String = DEFAULT_BODY,
    @Exclude
    var id: String = DEFAULT_ID
) {
    companion object {
        const val DEFAULT_ID = "Default ID"
        const val DEFAULT_BODY = "Default Body"
        const val DEFAULT_HEAD = "Default Head"
    }

    constructor() : this("", "", "")
}