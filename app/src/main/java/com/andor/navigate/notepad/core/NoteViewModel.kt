package com.andor.navigate.notepad.core

import android.app.Application
import androidx.lifecycle.*
import com.andor.navigate.notepad.listing.dao.NoteModel
import com.andor.navigate.notepad.listing.dao.NoteRepoImpl
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class NoteViewModel(application: Application, uid: String, db: FirebaseFirestore) :
    AndroidViewModel(application) {
    private val appStateRelay = MutableLiveData<AppState>(AppState(currentUserID = uid))

    private val appEventRelay = MutableLiveData<Event<EventOnFragment>>(Event(EventOnFragment.None))

    private val appAlertRelay = MutableLiveData<Event<AlertEvent>>(Event(AlertEvent.None))

    private val repository: NoteRepoImpl = NoteRepoImpl(db) {
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

    fun actionAddNote(
        addNoteEvent: EventOnFragment.AddNoteEvent
    ) {
        val noteModel = when (addNoteEvent) {
            is EventOnFragment.AddNoteEvent.AddNote -> addNoteEvent.noteModel
            is EventOnFragment.AddNoteEvent.UpdateNote -> addNoteEvent.noteModel
            else -> null
        }
        if (noteModel != null && (noteModel.head.isEmpty() || noteModel.head.isBlank())) {
            appAlertRelay.postValue(Event(AlertEvent.TitleEmptyToast))
            appEventRelay.postValue(Event(EventOnFragment.None))
        } else {
            when (addNoteEvent) {
                is EventOnFragment.AddNoteEvent.AddNote -> {
                    appStateRelay.value =
                        appStateRelay.value!!.copy(
                            selectedNote = addNoteEvent.noteModel
                        )
                    insert(addNoteEvent.noteModel)
                    appEventRelay.postValue(Event(addNoteEvent))
                }
                is EventOnFragment.AddNoteEvent.UpdateNote -> {
                    appStateRelay.value =
                        appStateRelay.value!!.copy(
                            selectedNote = addNoteEvent.noteModel
                        )
                    insert(addNoteEvent.noteModel)
                    appEventRelay.postValue(Event(addNoteEvent))
                }
                is EventOnFragment.AddNoteEvent.Cancel -> {
                    appEventRelay.postValue(Event(addNoteEvent))
                }
            }
        }
    }

    fun updateSelectedNotes(noteModel: NoteModel) {
        appStateRelay.value = appStateRelay.value!!.copy(selectedNote = noteModel)
    }

    fun changeSortingType(sortingType: SortingType) {
        appStateRelay.value = appStateRelay.value!!.copy(sortingType = sortingType)
    }


    fun getAppStateStream(): LiveData<AppState> = appStateRelay
    fun getAppEventStream(): LiveData<Event<EventOnFragment>> = appEventRelay
    fun getAppAlertStream(): LiveData<Event<AlertEvent>> = appAlertRelay
}


class NoteViewModelFactory(private val application: Application, val uid: String) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return NoteViewModel(application, uid, FirebaseFirestore.getInstance()) as T
    }
}

sealed class RepoEvent {
    data class InsertNoteModel(val noteModel: NoteModel) : RepoEvent()
    data class UpdateAllNoteModel(val noteModelList: ArrayList<NoteModel>) : RepoEvent()
}