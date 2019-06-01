package com.andor.navigate.notepad.listing.fragment

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.andor.navigate.notepad.listing.dao.NoteModel
import com.andor.navigate.notepad.listing.dao.NoteRepoImpl
import com.andor.navigate.notepad.listing.dao.NoteRoomDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class NoteViewModel(application: Application) : AndroidViewModel(application) {

    val selectedNote = MutableLiveData<NoteModel>()
    private val repository: NoteRepoImpl
    val allNotes: LiveData<HashMap<String, NoteModel>>

    init {
        val noteDao = NoteRoomDatabase.getDatabase(application, viewModelScope).noteDao()
        repository = NoteRepoImpl(noteDao)
        allNotes = repository.allNotes
    }

    fun insert(note: NoteModel) = viewModelScope.launch(Dispatchers.IO) {
        repository.insert(note)
    }

    fun delete(selectedNotes: HashSet<String>) = viewModelScope.launch(Dispatchers.IO) {
        repository.delete(selectedNotes)
    }

    fun addNewUserData(uid: String) {
        repository.insertUser(uid)
    }

    fun fetchUserNotes() = viewModelScope.launch(Dispatchers.IO) {
        repository.getAllNotes()
    }

}