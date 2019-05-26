package com.andor.navigate.notepad.listing.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface NoteDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(noteModel: NoteModel)

    @Query("DELETE from note_table")
    fun deleteAll()

    @Query("SELECT * from note_table ORDER BY head ASC")
    fun getAllNotes(): LiveData<List<NoteModel>>
}