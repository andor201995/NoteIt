package com.andor.navigate.notepad.listing

interface NoteRepo {
    fun getNotes(): HashMap<Int,ListModel>
}
