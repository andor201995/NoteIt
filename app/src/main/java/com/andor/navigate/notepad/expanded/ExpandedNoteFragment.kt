package com.andor.navigate.notepad.expanded


import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.view.*
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment.findNavController
import com.andor.navigate.notepad.R
import com.andor.navigate.notepad.core.AppState
import com.andor.navigate.notepad.core.NoteViewModel
import com.andor.navigate.notepad.core.Utils
import com.andor.navigate.notepad.core.navigateSafe
import com.andor.navigate.notepad.listing.NotesActivity
import kotlinx.android.synthetic.main.fragment_expanded_note.*


class ExpandedNoteFragment : Fragment() {

    private lateinit var oldAppState: AppState
    private lateinit var viewModel: NoteViewModel

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel = ViewModelProvider(activity!!).get(NoteViewModel::class.java)

        expandedNoteTxt.movementMethod = ScrollingMovementMethod()
        setDoubleTapListener()

        viewModel.getAppStateStream().observe(viewLifecycleOwner, Observer { appState ->

            appState.selectedNote?.let {
                expandedNoteTxt.setText(it.body, TextView.BufferType.EDITABLE)
                (activity as NotesActivity).setActionBarTitle(it.head)
                view!!.background = Utils.getBackGroundRes(context!!, it.bg)
            }
            oldAppState = appState
        })
    }

    private fun setDoubleTapListener() {
        val gestureDetector =
            GestureDetector(context, object : GestureDetector.SimpleOnGestureListener() {
                override fun onDoubleTap(e: MotionEvent): Boolean {
                    operEditor()
                    return true
                }
            })
        expandedNoteTxt.setOnTouchListener { _, event ->
            gestureDetector.onTouchEvent(event)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        setHasOptionsMenu(true)
        return inflater.inflate(R.layout.fragment_expanded_note, container, false)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.expanded_note_option_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.edit) {
            setBottomMenu()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setBottomMenu() {
        findNavController(this).navigateSafe(
            R.id.expandedNoteFragment,
            R.id.action_expandedNoteFragment_to_addNewNoteFragment
        )
    }

    private fun operEditor() {
        val action =
            ExpandedNoteFragmentDirections.actionExpandedNoteFragmentToUpdateNoteFragment(true)
        findNavController(this).navigateSafe(R.id.expandedNoteFragment, action)
    }

}
