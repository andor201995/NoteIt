package com.andor.navigate.notepad.core

import androidx.recyclerview.widget.DiffUtil
import com.andor.navigate.notepad.listing.dao.NoteModel

class MyDiffCallback(
    private val oldNoteList: List<NoteModel>,
    private val newNoteList: List<NoteModel>
) :
    DiffUtil.Callback() {

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldNoteList[oldItemPosition].id == newNoteList[newItemPosition].id
    }

    override fun getOldListSize(): Int {
        return oldNoteList.size
    }

    override fun getNewListSize(): Int {
        return newNoteList.size
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldNoteList[oldItemPosition] == newNoteList[newItemPosition]
    }

}