package com.andor.navigate.notepad.listing.fragment


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.andor.navigate.notepad.R
import com.andor.navigate.notepad.core.NoteViewModel
import com.andor.navigate.notepad.listing.dao.NoteModel
import kotlinx.android.synthetic.main.fragment_add_new_note.*
import java.util.*


class AddNewNoteFragment : Fragment() {

    private lateinit var viewModel: NoteViewModel

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
        newNoteButtonAccept.setOnClickListener {
            val newUUID = UUID.randomUUID().toString()
            viewModel.appStateRelay.postValue(
                viewModel.appStateRelay.value!!.copy(
                    selectedNote = NoteModel(
                        newNoteHeadText.text.toString(),
                        id = newUUID
                    )
                )
            )
//            val action = NoteListingFragmentDirections.actionNoteListingFragmentToUpdateNoteFragment()
//            Navigation.findNavController(view!!).navigate(action)
            //have to think about this
        }
    }

}
