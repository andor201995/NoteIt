package com.andor.navigate.notepad.listing.fragment


import android.content.Context
import android.os.Bundle
import android.view.*
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.andor.navigate.notepad.R
import com.andor.navigate.notepad.core.*
import com.andor.navigate.notepad.listing.NotesActivity
import com.andor.navigate.notepad.listing.adapter.ListItemEvent
import com.andor.navigate.notepad.listing.adapter.ListingAdapter
import com.andor.navigate.notepad.listing.dao.NoteModel
import kotlinx.android.synthetic.main.fragment_note_listing.*


class NoteListingFragment : Fragment() {
    private lateinit var oldAppState: AppState
    private val bottomSheetMenuFragment = BottomSheetMenuFragment()
    private var longPressActionMode: androidx.appcompat.view.ActionMode? = null
    private var isLongPressed: Boolean = false
    private val selectedNotes: HashSet<NoteModel> = HashSet()
    private lateinit var viewModel: NoteViewModel
    private val gridSize = 2

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(activity!!).get(NoteViewModel::class.java)
        hideKeyBoard()
        bindAppStateStream()
        viewModel.fetchUserNotes()
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        setHasOptionsMenu(true)
        return inflater.inflate(R.layout.fragment_note_listing, container, false)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)

        if (isLongPressed) {
            val actionModeCallback = object : androidx.appcompat.view.ActionMode.Callback {
                override fun onActionItemClicked(mode: androidx.appcompat.view.ActionMode?, item: MenuItem): Boolean {
                    return onOptionsItemSelected(item)
                }

                override fun onCreateActionMode(
                    actionBarMenuItems: androidx.appcompat.view.ActionMode?,
                    menu: Menu?
                ): Boolean {
                    this@NoteListingFragment.longPressActionMode = actionBarMenuItems
                    actionBarMenuItems?.menuInflater?.inflate(R.menu.menu_item_long_press, menu)
                    return true
                }

                override fun onPrepareActionMode(mode: androidx.appcompat.view.ActionMode?, menu: Menu?): Boolean {
                    return true
                }

                override fun onDestroyActionMode(mode: androidx.appcompat.view.ActionMode?) {
                    this@NoteListingFragment.longPressActionMode = null
                    selectedNotes.clear()
                    isLongPressed = false
                    activity?.invalidateOptionsMenu()
                    listRecyclerView.adapter?.notifyDataSetChanged()
                }

            }
            if (longPressActionMode == null) {
                (activity!! as NotesActivity).startSupportActionMode(actionModeCallback)
            }
        } else {
            inflater.inflate(R.menu.menu_main, menu)
        }
    }

    private fun sendAddNoteBottomSheetCommand(menuType: BottomMenuType) {
        if (!bottomSheetMenuFragment.isAdded) {
            viewModel.openBottomMenu(menuType)
        } else {
            bottomSheetMenuFragment.dismiss()
        }
    }

    private fun hideKeyBoard() {
        val imm = activity!!.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view!!.windowToken, 0)
    }

    private fun bindAppStateStream() {
        viewModel.appStateRelay.observe(this, Observer { appState ->
            appState?.let { notNullAppState ->
                updateRecyclerView(notNullAppState)
                handleBottomSheetEvent(notNullAppState)

                oldAppState = appState
            }
        })
    }

    private fun handleBottomSheetEvent(appState: AppState) {
        val bottomMenuEvent = appState.bottomMenuEvent
        bottomMenuEvent.getContentIfNotHandled()?.let {
            if (::oldAppState.isInitialized && oldAppState.bottomMenuEvent != bottomMenuEvent) {
                when (it) {
                    is BottomMenuEvent.AddNote -> {
                        bottomSheetMenuFragment.dismiss()
                        Navigation.findNavController(view!!)
                            .navigate(R.id.action_noteListingFragment_to_updateNoteFragment)
                    }
                    is BottomMenuEvent.Close -> if (bottomSheetMenuFragment.isAdded) bottomSheetMenuFragment.dismiss()
                    is BottomMenuEvent.Open -> {
                        if (!bottomSheetMenuFragment.isAdded) {
                            bottomSheetMenuFragment.show(
                                activity!!.supportFragmentManager,
                                "Bottom_Sheet"
                            )
                        }
                    }
                }
            }
        }
    }

    private fun updateRecyclerView(
        appState: AppState
    ) {
        val noteList = appState.listOfAllNotes
        if (listRecyclerView.adapter == null) {
            val listingAdapter = ListingAdapter(context!!, noteList) {
                setRecyclerViewEventListener(it)
            }
            setRecyclerViewManager(appState)
            listRecyclerView.adapter = listingAdapter

        } else {
            if (::oldAppState.isInitialized && appState.listingType != oldAppState.listingType) {
                setRecyclerViewManager(appState)
            }
            (listRecyclerView.adapter as ListingAdapter).updateRecyclerView(noteList)
        }
    }

    private fun setRecyclerViewEventListener(it: ListItemEvent) {
        when (it) {
            is ListItemEvent.SingleClickEvent -> {
                if (!isLongPressed) {
                    viewModel.updateSelectedNotes(it.noteModel)
                    val action =
                        NoteListingFragmentDirections.actionNoteListingFragmentToExpandedNoteFragment()
                    Navigation.findNavController(view!!).navigate(action)
                } else {
                    if (selectedNotes.contains(it.noteModel)) {
                        selectedNotes.remove(it.noteModel)
                        if (selectedNotes.size == 0) {
                            longPressActionMode?.finish()
                        }
                    } else {
                        selectedNotes.add(it.noteModel)
                    }
                }
            }
            is ListItemEvent.LongClickEvent -> {
                isLongPressed = true
                selectedNotes.add(it.noteModel)
                activity!!.invalidateOptionsMenu()
            }
        }
    }

    private fun setRecyclerViewManager(
        appState: AppState
    ) {
        listRecyclerView.layoutManager = when (appState.listingType) {
            is ListingType.Linear -> {
                val linearLayoutManager = LinearLayoutManager(context)
                linearLayoutManager.orientation = RecyclerView.VERTICAL
                linearLayoutManager
            }
            ListingType.Grid -> {
                val gridLayoutManager = GridLayoutManager(context, gridSize)
                gridLayoutManager
            }
            ListingType.Staggered -> {
                StaggeredGridLayoutManager(gridSize, RecyclerView.VERTICAL)
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.SelectedItemDelete) {
            viewModel.delete(HashSet(selectedNotes))
            longPressActionMode?.finish()
        }
        if (item.itemId == R.id.action_setting) {
            sendAddNoteBottomSheetCommand(BottomMenuType.Setting)
        }
        if (item.itemId == R.id.action_add) {
            sendAddNoteBottomSheetCommand(BottomMenuType.AddNote)
        }
        return super.onOptionsItemSelected(item)
    }
}
