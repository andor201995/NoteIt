package com.andor.navigate.notepad.expanded


import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.NavHostFragment.findNavController
import com.andor.navigate.notepad.R
import com.andor.navigate.notepad.core.*
import com.andor.navigate.notepad.listing.NotesActivity
import com.andor.navigate.notepad.listing.fragment.BottomSheetMenuFragment
import kotlinx.android.synthetic.main.fragment_expanded_note.*


class ExpandedNoteFragment : Fragment() {

    private lateinit var oldAppState: AppState
    private lateinit var viewModel: NoteViewModel
    private val bottomSheetMenuFragment = BottomSheetMenuFragment()

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel = ViewModelProviders.of(activity!!).get(NoteViewModel::class.java)

        expandedNoteTxt.movementMethod = ScrollingMovementMethod()
        setDoubleTapListener()

        viewModel.getAppStateStream().observe(viewLifecycleOwner, Observer { appState ->

            appState.selectedNote?.let {
                expandedNoteTxt.text = it.body
                (activity as NotesActivity).setActionBarTitle(it.head)
                view!!.background = Utils.getBackGroundRes(context!!, it.bg)
                handleBottomSheet(appState)
            }
            handleBottomSheetEvent(appState.bottomMenuEvent)
            oldAppState = appState
        })
    }

    private fun handleBottomSheetEvent(bottomMenuEvent: Event<BottomMenuEvent>) {
        bottomMenuEvent.getContentIfNotHandled()?.let {
            when (it) {
                is BottomMenuEvent.Close -> if (bottomSheetMenuFragment.isAdded) bottomSheetMenuFragment.dismiss()
                is BottomMenuEvent.AddNote -> if (bottomSheetMenuFragment.isAdded) bottomSheetMenuFragment.dismiss()
            }
        }
    }

    private fun handleBottomSheet(appState: AppState) {
        if (::oldAppState.isInitialized
            && ((oldAppState.bottomMenuType != appState.bottomMenuType && appState.bottomMenuType is BottomMenuType.AddNote)
                    || (oldAppState.bottomMenuEvent != appState.bottomMenuEvent && appState.bottomMenuEvent.peekContent() is BottomMenuEvent.Open))
        ) {
            openBottomMenu()
        }
    }

    private fun openBottomMenu() {
        if (!bottomSheetMenuFragment.isAdded) {
            bottomSheetMenuFragment.showNow(
                activity!!.supportFragmentManager,
                "Bottom_Sheet_add_note"
            )
        }
    }

    private fun setDoubleTapListener() {
        val gestureDetector = GestureDetector(context, object : GestureDetector.SimpleOnGestureListener() {
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
        viewModel.openBottomMenu(BottomMenuType.AddNote(isNewNote = false))
    }

    private fun operEditor() {
        val action =
            ExpandedNoteFragmentDirections.actionExpandedNoteFragmentToUpdateNoteFragment(true)
        findNavController(this).navigate(action)
    }

}
