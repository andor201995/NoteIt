package com.andor.navigate.notepad.listing

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.andor.navigate.notepad.R
import kotlinx.android.synthetic.main.fragment_add_note.*


class AddNoteFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_note, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        submitNote.setOnClickListener { view ->
            NoteRepoImpl.addNote(
                ListModel(
                    AddNoteFragmentArgs.fromBundle(arguments!!).headText,
                    newNoteBodyTxt.text.toString()
                )
            )
            Navigation.findNavController(view).navigateUp()
        }
    }
}
