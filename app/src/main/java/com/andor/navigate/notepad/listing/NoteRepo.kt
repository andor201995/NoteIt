package com.andor.navigate.notepad.listing

interface NoteRepo {
    fun getNotes(): HashMap<Int, ListModel>
    fun addNote(listModel: ListModel)
    fun deleteNote(indexKey: Int)
    fun updateNote(indexKey: Int, listModel: ListModel)
}
