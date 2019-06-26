package com.andor.navigate.notepad.core

import android.app.Application
import androidx.lifecycle.*
import com.andor.navigate.notepad.listing.dao.NoteModel
import com.andor.navigate.notepad.listing.dao.NoteRepoImpl
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class NoteViewModel(application: Application, private val repository: NoteRepoImpl) : AndroidViewModel(application) {
    private val appStateRelay = repository.getAppRelay()

    fun insert(note: NoteModel) = viewModelScope.launch(Dispatchers.IO) {
        repository.insert(note)
        appStateRelay.postValue(appStateRelay.value!!.copy(selectedNote = note))
    }

    fun delete(selectedNotes: HashSet<NoteModel>) = viewModelScope.launch(Dispatchers.IO) {
        repository.delete(selectedNotes)
    }

    fun fetchUserNotes() = viewModelScope.launch(Dispatchers.IO) {
        repository.getAllNotes()
    }

    fun dismissBottomSheet() {
        appStateRelay.value = appStateRelay.value!!.copy(
            bottomMenuType = BottomMenuType.None,
            bottomMenuEvent = Event(BottomMenuEvent.Close)
        )
    }

    fun changeListTypeTo(listType: ListingType) {
        appStateRelay.value = appStateRelay.value!!.copy(listingType = listType)
    }

    fun actionAddNote(newNoteModel: NoteModel) {
        appStateRelay.value =
            appStateRelay.value!!.copy(
                selectedNote = newNoteModel, bottomMenuEvent = Event(BottomMenuEvent.AddNote)
            )
        insert(newNoteModel)
    }

    fun openBottomMenu(menuType: BottomMenuType) {
        appStateRelay.value = appStateRelay.value!!.copy(
            bottomMenuEvent = Event(BottomMenuEvent.Open),
            bottomMenuType = menuType
        )
    }

    fun updateSelectedNotes(noteModel: NoteModel) {
        appStateRelay.value = appStateRelay.value!!.copy(selectedNote = noteModel)
    }

    fun getAppStateStream(): LiveData<AppState> = appStateRelay


}


class NoteViewModelFactory(val application: Application, val repository: NoteRepoImpl) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return NoteViewModel(application, repository) as T
    }
}
