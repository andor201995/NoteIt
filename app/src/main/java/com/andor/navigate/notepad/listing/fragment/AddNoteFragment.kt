package com.andor.navigate.notepad.listing.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import com.andor.navigate.notepad.R
import com.andor.navigate.notepad.listing.dao.NoteModel
import kotlinx.android.synthetic.main.fragment_add_note.*


class AddNoteFragment : Fragment() {

    private lateinit var viewModel: NoteViewModel

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(activity!!).get(NoteViewModel::class.java)
        submitNote.setOnClickListener { view ->
            viewModel.insert(
                NoteModel(
                    AddNoteFragmentArgs.fromBundle(arguments!!).headText,
                    newNoteBodyTxt.text.toString()
                )
            )
            Navigation.findNavController(view).navigateUp()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_note, container, false)
    }

}
