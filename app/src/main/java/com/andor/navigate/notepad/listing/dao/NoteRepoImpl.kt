package com.andor.navigate.notepad.listing.dao

import androidx.annotation.WorkerThread
import androidx.lifecycle.MutableLiveData
import com.andor.navigate.notepad.auth.UserAuthentication
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions


class NoteRepoImpl(private val noteDao: NoteDao) {
    private val db = FirebaseFirestore.getInstance()
    private val userAuth = UserAuthentication.fireBaseAuth.currentUser
    val allNotes: MutableLiveData<HashMap<String, NoteModel>> = MutableLiveData()

    @WorkerThread
    fun insert(noteModel: NoteModel) {
//        noteDao.insert(noteModel)
        db.collection("Users").document(userAuth!!.uid).collection("Notes")
            .document()
            .set(
                hashMapOf(
                    "head" to noteModel.noteHead,
                    "body" to noteModel.noteBody
                ), SetOptions.merge()
            ).addOnSuccessListener {
                getAllNotes()
            }
    }

    fun delete(selectedNotes: HashSet<String>) {
//        noteDao.delete(selectedNotes)
        selectedNotes.forEach {
            db.collection("Users").document(userAuth!!.uid).collection("Notes")
                .document(it).delete().addOnSuccessListener {
                    getAllNotes()
                }
        }
    }

    @WorkerThread
    fun getAllNotes() {
        db.collection("Users").document(userAuth!!.uid).collection("Notes")
            .get()
            .addOnSuccessListener { collection ->
                val hashMap = HashMap<String, NoteModel>()
                collection.documents.forEach {
                    hashMap.put(
                        it.id,
                        NoteModel(
                            it.get("head").toString(),
                            it.get("body").toString()
                        )
                    )
                }
                allNotes.postValue(hashMap)
            }
    }

    fun insertUser(uid: String) {
        val defaultData = hashMapOf("head" to "Hello", "body" to "Welcome to NoteIt...")
        db.collection("Users").document(uid).collection("Notes").document("default")
            .set(defaultData)
    }

}
