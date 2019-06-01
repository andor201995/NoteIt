package com.andor.navigate.notepad.listing.dao

import androidx.annotation.WorkerThread
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions


class NoteRepoImpl() {
    private val db = FirebaseFirestore.getInstance()
    val allNotes: MutableLiveData<HashMap<String, NoteModel>> = MutableLiveData()

    @WorkerThread
    fun insert(
        noteModel: NoteModel,
        uid: String
    ) {
        db.collection("Users").document(uid).collection("Notes")
            .document()
            .set(
                hashMapOf(
                    "head" to noteModel.noteHead,
                    "body" to noteModel.noteBody
                ), SetOptions.merge()
            ).addOnSuccessListener {
                getAllNotes(uid)
            }
    }

    fun delete(selectedNotes: HashSet<String>, uid: String) {
        selectedNotes.forEach {
            db.collection("Users").document(uid).collection("Notes")
                .document(it).delete().addOnSuccessListener {
                    getAllNotes(uid)
                }
        }
    }

    @WorkerThread
    fun getAllNotes(uid: String) {
        db.collection("Users").document(uid).collection("Notes")
            .get()
            .addOnSuccessListener { collection ->
                val hashMap = HashMap<String, NoteModel>()
                collection.documents.forEach {
                    hashMap[it.id] = NoteModel(
                        it.get("head").toString(),
                        it.get("body").toString()
                    )
                }
                allNotes.postValue(hashMap)
            }
    }
}
