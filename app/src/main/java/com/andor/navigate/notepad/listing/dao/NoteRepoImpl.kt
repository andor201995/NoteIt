package com.andor.navigate.notepad.listing.dao

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData

class NoteRepoImpl(private val noteDao: NoteDao) {
    val allNotes: LiveData<List<NoteModel>> = noteDao.getAllNotes()

    @WorkerThread
    suspend fun insert(noteModel: NoteModel) {
        noteDao.insert(noteModel)
    }

}
