package com.andor.navigate.notepad.listing.dao

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "note_table")
data class NoteModel(
    @PrimaryKey @ColumnInfo(name = "head")
    val noteHead: String = "Default Head",

    @ColumnInfo(name = "body")
    val noteBody: String = "Default Body"
)