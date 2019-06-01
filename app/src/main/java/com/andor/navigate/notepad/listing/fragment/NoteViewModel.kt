package com.andor.navigate.notepad.listing.fragment

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.andor.navigate.notepad.listing.dao.NoteModel
import com.andor.navigate.notepad.listing.dao.NoteRepoImpl
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class NoteViewModel(application: Application) : AndroidViewModel(application) {

    lateinit var uid: String
    val selectedNote = MutableLiveData<NoteModel>()
    val allNotes: LiveData<HashMap<String, NoteModel>>
    private val repository: NoteRepoImpl = NoteRepoImpl()

    init {
        allNotes = repository.allNotes
    }

    fun insert(note: NoteModel) = viewModelScope.launch(Dispatchers.IO) {
        repository.insert(note, uid)
    }

    fun delete(selectedNotes: HashSet<String>) = viewModelScope.launch(Dispatchers.IO) {
        repository.delete(selectedNotes, uid)
    }

    fun fetchUserNotes() = viewModelScope.launch(Dispatchers.IO) {
        repository.getAllNotes(uid)
    }

}