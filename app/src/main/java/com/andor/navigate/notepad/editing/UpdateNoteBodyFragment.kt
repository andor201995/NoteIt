package com.andor.navigate.notepad.editing

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import com.andor.navigate.notepad.MainActivity
import com.andor.navigate.notepad.R
import com.andor.navigate.notepad.listing.dao.NoteModel
import com.andor.navigate.notepad.listing.fragment.NoteViewModel
import kotlinx.android.synthetic.main.fragment_update_note.*


class UpdateNoteBodyFragment : Fragment() {

    private lateinit var viewModel: NoteViewModel

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(activity!!).get(NoteViewModel::class.java)

        if (UpdateNoteBodyFragmentArgs.fromBundle(arguments!!).editMode) {
            val bodyText = viewModel.selectedNote.value?.noteBody
            newNoteBodyTxt.setText(bodyText, TextView.BufferType.EDITABLE)
        }

        submitNote.setOnClickListener { view ->
            val noteModel = viewModel.selectedNote.value?.noteHead?.let {
                NoteModel(
                    it,
                    newNoteBodyTxt.text.toString()
                )
            }
            noteModel?.let { viewModel.insert(it) }
            viewModel.selectedNote.postValue(noteModel)
            Navigation.findNavController(view).navigateUp()
        }
        val headText = viewModel.selectedNote.value?.noteHead
        headText?.let { (activity as MainActivity).setActionBarTitle(it) }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_update_note, container, false)
    }

}
