package com.andor.navigate.notepad.expanded


import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import com.andor.navigate.notepad.MainActivity
import com.andor.navigate.notepad.R
import com.andor.navigate.notepad.listing.fragment.NoteViewModel
import kotlinx.android.synthetic.main.fragment_expanded_note.*


class ExpandedNoteFragment : Fragment() {

    private lateinit var viewModel: NoteViewModel


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(activity!!).get(NoteViewModel::class.java)
        expandedNoteTxt.movementMethod = ScrollingMovementMethod()
        viewModel.selectedNote.observe(this, Observer {
            expandedNoteTxt.text = it.noteBody
            (activity as MainActivity).setActionBarTitle(it.noteHead)
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        setHasOptionsMenu(true)
        return inflater.inflate(R.layout.fragment_expanded_note, container, false)
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater!!.inflate(R.menu.expanded_note_option_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item!!.itemId == R.id.edit) {
            val action =
                ExpandedNoteFragmentDirections.actionExpandedNoteFragmentToUpdateNoteFragment(true)
            Navigation.findNavController(view!!).navigate(action)
        }
        return super.onOptionsItemSelected(item)
    }
}
