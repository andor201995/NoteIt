package com.andor.navigate.notepad.listing.fragment


import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.andor.navigate.notepad.R
import com.andor.navigate.notepad.core.BottomMenuType
import com.andor.navigate.notepad.core.NoteViewModel
import com.andor.navigate.notepad.core.Utils
import com.andor.navigate.notepad.listing.dao.NoteModel
import kotlinx.android.synthetic.main.fragment_add_new_note.*
import java.util.*


class AddNewNoteFragment : Fragment() {

    private lateinit var viewModel: NoteViewModel
    private var selectedBGType: String = NoteModel.NOTE_BG0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_new_note, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(activity!!).get(NoteViewModel::class.java)
        viewModel.getAppStateStream().value!!.let {
            if (it.bottomMenuType is BottomMenuType.AddNote && !it.bottomMenuType.isNewNote) {
                view!!.background = Utils.getBackGroundRes(context!!, it.selectedNote!!.bg)
                newNoteHeadText.setText(it.selectedNote.head, TextView.BufferType.EDITABLE)
                oldNoteUpdateButton.visibility = View.VISIBLE
                newNoteButtonAccept.visibility = View.GONE
            } else {
                oldNoteUpdateButton.visibility = View.GONE
                newNoteButtonAccept.visibility = View.VISIBLE
            }
            setButtonClickListener()
        }
    }

    private fun setButtonClickListener() {
        newNoteButtonAccept.setOnClickListener {
            val noteModel = NoteModel(
                id = UUID.randomUUID().toString(),
                head = newNoteHeadText.text.toString(),
                bg = selectedBGType
            )
            viewModel.actionAddNote(noteModel)
            viewModel.dismissBottomSheet()

        }
        newNoteButtonCancel.setOnClickListener {
            viewModel.dismissBottomSheet()
        }

        oldNoteUpdateButton.setOnClickListener {
            val noteModel = viewModel.getAppStateStream().value!!.selectedNote!!
            viewModel.actionAddNote(
                noteModel.copy(
                    head = newNoteHeadText.text.toString(), bg = selectedBGType
                )
            )
            viewModel.dismissBottomSheet()
        }
        setNoteBackGroundButtonListener()
    }

    private fun setNoteBackGroundButtonListener() {
        btn_bg_0.setOnClickListener {
            clearButtonBackGround()
            btn_bg_0.background = ContextCompat.getDrawable(context!!, R.drawable.backgound_button_select)
            selectedBGType = NoteModel.NOTE_BG0
            addFragmentContainer.background = ContextCompat.getDrawable(context!!, R.drawable.background_note_0)
        }
        btn_bg_1.setOnClickListener {
            clearButtonBackGround()
            btn_bg_1.background = ContextCompat.getDrawable(context!!, R.drawable.backgound_button_select)
            selectedBGType = NoteModel.NOTE_BG1
            addFragmentContainer.background = ContextCompat.getDrawable(context!!, R.drawable.background_note_1)
        }
        btn_bg_2.setOnClickListener {
            clearButtonBackGround()
            btn_bg_2.background = ContextCompat.getDrawable(context!!, R.drawable.backgound_button_select)
            selectedBGType = NoteModel.NOTE_BG2
            addFragmentContainer.background = ContextCompat.getDrawable(context!!, R.drawable.background_note_2)
        }
        btn_bg_3.setOnClickListener {
            clearButtonBackGround()
            btn_bg_3.background = ContextCompat.getDrawable(context!!, R.drawable.backgound_button_select)
            selectedBGType = NoteModel.NOTE_BG3
            addFragmentContainer.background = ContextCompat.getDrawable(context!!, R.drawable.background_note_3)
        }
        btn_bg_4.setOnClickListener {
            clearButtonBackGround()
            btn_bg_4.background = ContextCompat.getDrawable(context!!, R.drawable.backgound_button_select)
            selectedBGType = NoteModel.NOTE_BG4
            addFragmentContainer.background = ContextCompat.getDrawable(context!!, R.drawable.background_note_4)
        }
        btn_bg_5.setOnClickListener {
            clearButtonBackGround()
            btn_bg_5.background = ContextCompat.getDrawable(context!!, R.drawable.backgound_button_select)
            selectedBGType = NoteModel.NOTE_BG5
            addFragmentContainer.background = ContextCompat.getDrawable(context!!, R.drawable.background_note_5)
        }

    }

    private fun clearButtonBackGround() {
        btn_bg_0.setBackgroundColor(Color.TRANSPARENT)
        btn_bg_1.setBackgroundColor(Color.TRANSPARENT)
        btn_bg_2.setBackgroundColor(Color.TRANSPARENT)
        btn_bg_3.setBackgroundColor(Color.TRANSPARENT)
        btn_bg_4.setBackgroundColor(Color.TRANSPARENT)
        btn_bg_5.setBackgroundColor(Color.TRANSPARENT)
    }

}
