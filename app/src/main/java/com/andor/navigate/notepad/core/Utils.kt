package com.andor.navigate.notepad.core

import android.content.Context
import android.graphics.drawable.Drawable
import androidx.core.content.ContextCompat
import com.andor.navigate.notepad.R
import com.andor.navigate.notepad.listing.dao.NoteModel

class Utils {
    companion object {
        fun getBackGroundRes(context: Context, bgString: String): Drawable? {
            val rid: Int = when {
                bgString == NoteModel.NOTE_BG1 -> R.drawable.background_note_1
                bgString == NoteModel.NOTE_BG2 -> R.drawable.background_note_2
                bgString == NoteModel.NOTE_BG3 -> R.drawable.background_note_3
                bgString == NoteModel.NOTE_BG4 -> R.drawable.background_note_4
                bgString == NoteModel.NOTE_BG5 -> R.drawable.background_note_5
                else -> R.drawable.background_note_0
            }
            return ContextCompat.getDrawable(context, rid)
        }
    }
}