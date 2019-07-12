package com.andor.navigate.notepad.core

import android.app.Application
import androidx.lifecycle.*
import com.andor.navigate.notepad.listing.dao.NoteModel
import com.andor.navigate.notepad.listing.dao.NoteRepoImpl
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class NoteViewModel(application: Application, uid: String) : AndroidViewModel(application) {
    private val appStateRelay = MutableLiveData<AppState>(AppState(currentUserID = uid))

    private val repository: NoteRepoImpl = NoteRepoImpl {
        when (it) {
            is RepoEvent.UpdateAllNoteModel -> {
                appStateRelay.postValue(appStateRelay.value!!.copy(listOfAllNotes = it.noteModelList))
            }
            is RepoEvent.InsertNoteModel -> {
                appStateRelay.postValue(appStateRelay.value!!.copy(selectedNote = it.noteModel))
            }
        }
    }

    fun insert(note: NoteModel) = viewModelScope.launch(Dispatchers.IO) {
        repository.insert(note, appStateRelay.value!!.currentUserID)
        appStateRelay.postValue(appStateRelay.value!!.copy(selectedNote = note))
    }

    fun delete(selectedNotes: HashSet<NoteModel>) = viewModelScope.launch(Dispatchers.IO) {
        repository.delete(selectedNotes, appStateRelay.value!!.currentUserID)
    }

    fun fetchUserNotes() = viewModelScope.launch(Dispatchers.IO) {
        repository.getAllNotes(appStateRelay.value!!.currentUserID)
    }

    fun changeListTypeTo(listType: ListingType) {
        appStateRelay.value = appStateRelay.value!!.copy(listingType = listType)
    }

    fun actionAddNote(newNoteModel: NoteModel) {
        appStateRelay.value =
            appStateRelay.value!!.copy(
                selectedNote = newNoteModel
            )
        insert(newNoteModel)
    }

    fun updateSelectedNotes(noteModel: NoteModel) {
        appStateRelay.value = appStateRelay.value!!.copy(selectedNote = noteModel)
    }

    fun getAppStateStream(): LiveData<AppState> = appStateRelay


}


class NoteViewModelFactory(val application: Application, val uid: String) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return NoteViewModel(application, uid) as T
    }
}

sealed class RepoEvent {
    data class InsertNoteModel(val noteModel: NoteModel) : RepoEvent()
    data class UpdateAllNoteModel(val noteModelList: ArrayList<NoteModel>) : RepoEvent()
}