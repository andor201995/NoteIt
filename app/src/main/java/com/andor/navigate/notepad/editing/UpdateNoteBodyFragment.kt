package com.andor.navigate.notepad.editing

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.andor.navigate.notepad.R
import com.andor.navigate.notepad.core.NoteViewModel
import com.andor.navigate.notepad.core.Utils
import com.andor.navigate.notepad.listing.NotesActivity
import kotlinx.android.synthetic.main.fragment_update_note.*
import java.util.*


class UpdateNoteBodyFragment : Fragment() {

    private lateinit var viewModel: NoteViewModel

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(activity!!).get(NoteViewModel::class.java)

        viewModel.getAppStateStream().value?.let { appState ->

            appState.selectedNote?.let {
                if (UpdateNoteBodyFragmentArgs.fromBundle(arguments!!).editMode) {
                    val bodyText = it.body
                    newNoteBodyTxt.setText(bodyText, TextView.BufferType.EDITABLE)
                    setSaveAfterDebounceTime(newNoteBodyTxt)
                }
                val headText = it.head
                (activity as NotesActivity).setActionBarTitle(headText)
                view!!.background = Utils.getBackGroundRes(context!!, it.bg)
            }
        }
    }

    private fun setSaveAfterDebounceTime(editText: EditText) {
        editText.addTextChangedListener(
            object : TextWatcher {
                private var timer = Timer()
                private val DELAY: Long = 1000 // milliseconds
                override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
                override fun beforeTextChanged(
                    s: CharSequence,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }

                override fun afterTextChanged(s: Editable) {
                    timer.cancel()
                    timer = Timer()
                    timer.schedule(
                        object : TimerTask() {
                            override fun run() {
                                updateNoteModel(s)
                            }
                        },
                        DELAY
                    )
                }
            }
        )

    }

    private fun updateNoteModel(s: Editable) {
        val value = viewModel.getAppStateStream().value!!
        val newNoteModel =
            value.selectedNote!!.copy(body = s.toString(), dateUpdated = System.currentTimeMillis())
        if (value.selectedNote.body != s.toString()) {
            viewModel.insert(newNoteModel)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_update_note, container, false)
    }

    override fun onStop() {
        updateNoteModel(newNoteBodyTxt.editableText)
        super.onStop()
    }
}
