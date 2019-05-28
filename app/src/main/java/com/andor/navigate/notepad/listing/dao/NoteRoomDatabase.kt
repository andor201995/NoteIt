package com.andor.navigate.notepad.listing.dao

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(entities = [NoteModel::class], version = 1)
abstract class NoteRoomDatabase : RoomDatabase() {
    abstract fun noteDao(): NoteDao

    companion object {
        @Volatile
        private var INSTANCE: NoteRoomDatabase? = null

        fun getDatabase(context: Context, coroutineScope: CoroutineScope): NoteRoomDatabase {
            return INSTANCE ?: synchronized(this) {
                // Create database here
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    NoteRoomDatabase::class.java,
                    "Word_database"
                ).addCallback(NoteDatabaseCallback(coroutineScope)).build()
                instance
            }
        }
    }

    private class NoteDatabaseCallback(
        private val scope: CoroutineScope
    ) : RoomDatabase.Callback() {

        override fun onOpen(db: SupportSQLiteDatabase) {
            super.onOpen(db)
            INSTANCE?.let { database ->
                scope.launch(Dispatchers.IO) {
                    val noteDao = database.noteDao()
                    noteDao.deleteAll()
                    val note1 = NoteModel("Hello", "What's up")
                    noteDao.insert(note1)
                    val note2 = NoteModel("Hello", "What's up")
                    noteDao.insert(note2)
                }
            }
        }
    }
}