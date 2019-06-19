package com.andor.navigate.notepad.core

import android.app.Application
import androidx.lifecycle.*
import com.andor.navigate.notepad.listing.dao.NoteModel
import com.andor.navigate.notepad.listing.dao.NoteRepoImpl
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class NoteViewModel(application: Application, uid: String) : AndroidViewModel(application) {
    val appStateRelay = MutableLiveData<AppState>(AppState(currentUserID = uid))

    private val repository: NoteRepoImpl = NoteRepoImpl(appStateRelay)

    fun insert(note: NoteModel) = viewModelScope.launch(Dispatchers.IO) {
        repository.insert(note, appStateRelay.value!!.currentUserID)
    }

    fun delete(selectedNotes: HashSet<NoteModel>) = viewModelScope.launch(Dispatchers.IO) {
        repository.delete(selectedNotes, appStateRelay.value!!.currentUserID)
    }

    fun fetchUserNotes() = viewModelScope.launch(Dispatchers.IO) {
        repository.getAllNotes(appStateRelay.value!!.currentUserID)
    }

    fun changeListTypeTo(listType: ListingType) {
        appStateRelay.postValue(appStateRelay.value!!.copy(listingType = listType))
    }

    fun updateSelectedNotes(noteModel: NoteModel) {
        appStateRelay.postValue(appStateRelay.value!!.copy(selectedNote = noteModel))
    }

}


class NoteViewModelFactory(val application: Application, val uid: String) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return NoteViewModel(application, uid) as T
    }
}