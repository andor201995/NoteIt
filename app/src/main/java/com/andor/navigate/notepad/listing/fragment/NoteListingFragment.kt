package com.andor.navigate.notepad.listing.fragment


import android.app.SearchManager
import android.content.Context
import android.os.Bundle
import android.view.*
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.andor.navigate.notepad.R
import com.andor.navigate.notepad.core.AppState
import com.andor.navigate.notepad.core.ListingType
import com.andor.navigate.notepad.core.NoteViewModel
import com.andor.navigate.notepad.core.navigateSafe
import com.andor.navigate.notepad.listing.NotesActivity
import com.andor.navigate.notepad.listing.adapter.ListItemEvent
import com.andor.navigate.notepad.listing.adapter.ListingAdapter
import com.andor.navigate.notepad.listing.dao.NoteModel
import kotlinx.android.synthetic.main.fragment_note_listing.*


class NoteListingFragment : Fragment() {
    private lateinit var oldAppState: AppState
    private var longPressActionMode: androidx.appcompat.view.ActionMode? = null
    private var isLongPressed: Boolean = false
    private val selectedNotes: HashSet<NoteModel> = HashSet()
    private lateinit var viewModel: NoteViewModel
    private val gridSize = 2
    private lateinit var listingAdapter: ListingAdapter

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(activity!!).get(NoteViewModel::class.java)
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
                override fun onActionItemClicked(
                    mode: androidx.appcompat.view.ActionMode?,
                    item: MenuItem
                ): Boolean {
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

                override fun onPrepareActionMode(
                    mode: androidx.appcompat.view.ActionMode?,
                    menu: Menu?
                ): Boolean {
                    return true
                }

                override fun onDestroyActionMode(mode: androidx.appcompat.view.ActionMode?) {
                    this@NoteListingFragment.longPressActionMode = null
                    selectedNotes.clear()
                    isLongPressed = false
                    activity?.invalidateOptionsMenu()
                    this@NoteListingFragment.listingAdapter.notifyDataSetChanged()
                }

            }
            if (longPressActionMode == null) {
                (activity!! as NotesActivity).startSupportActionMode(actionModeCallback)
            }
        } else {
            inflater.inflate(R.menu.menu_main, menu)
            val searchManager = activity!!.getSystemService(Context.SEARCH_SERVICE)
            val searchActionMenuItem = menu.findItem(R.id.action_search)
            val settingActionMenuItem = menu.findItem(R.id.action_setting)
            val addActionMenuItem = menu.findItem(R.id.action_add)

            if (searchActionMenuItem is MenuItem) {
                searchActionMenuItem.setOnActionExpandListener(object :
                    MenuItem.OnActionExpandListener {
                    override fun onMenuItemActionExpand(p0: MenuItem?): Boolean {
                        settingActionMenuItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER)
                        addActionMenuItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER)
                        return true
                    }

                    override fun onMenuItemActionCollapse(p0: MenuItem?): Boolean {
                        activity?.invalidateOptionsMenu()
                        return true
                    }
                })
            }
            val searchView = searchActionMenuItem.actionView as SearchView
            searchView.apply {
                // Assumes current activity is the searchable activity
                if (searchManager is SearchManager) {
                    setSearchableInfo(searchManager.getSearchableInfo(activity!!.componentName))
                }
                setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                    override fun onQueryTextSubmit(query: String?): Boolean {
                        return true
                    }

                    override fun onQueryTextChange(newText: String?): Boolean {
                        this@NoteListingFragment.listingAdapter.filter.filter(newText)
                        return true
                    }

                })

            }

        }
    }

    private fun hideKeyBoard() {
        val imm = activity!!.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view!!.windowToken, 0)
    }

    private fun bindAppStateStream() {
        viewModel.getAppStateStream().observe(viewLifecycleOwner, Observer { appState ->
            appState?.let { notNullAppState ->
                updateRecyclerView(notNullAppState)
                oldAppState = appState
            }
        })
    }

    private fun updateRecyclerView(
        appState: AppState
    ) {
        val noteList = appState.listOfAllNotes
        if (listRecyclerView.adapter == null) {
            val listingAdapter = ListingAdapter(context!!, noteList) {
                setRecyclerViewEventListener(it)
            }
            listRecyclerView.adapter = listingAdapter
            this.listingAdapter = listingAdapter

            setRecyclerViewManager(appState)

        } else {
            //set List Type
            if (::oldAppState.isInitialized && appState.listingType != oldAppState.listingType) {
                setRecyclerViewManager(appState)
            }

            this.listingAdapter = (listRecyclerView.adapter as ListingAdapter)

            this.listingAdapter.updateRecyclerView(noteList)
        }
    }

    private fun setRecyclerViewEventListener(it: ListItemEvent) {
        when (it) {
            is ListItemEvent.SingleClickEvent -> {
                if (!isLongPressed) {
                    viewModel.updateSelectedNotes(it.noteModel)
                    val action =
                        NoteListingFragmentDirections.actionNoteListingFragmentToExpandedNoteFragment()
                    findNavController(this).navigateSafe(R.id.noteListingFragment, action)
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
        when (item.itemId) {
            R.id.SelectedItemDelete -> {
                viewModel.delete(HashSet(selectedNotes))
                longPressActionMode?.finish()
            }
            R.id.action_setting -> {
                activity?.invalidateOptionsMenu()
                findNavController(this).navigateSafe(
                    R.id.noteListingFragment,
                    R.id.action_noteListingFragment_to_settingFragment
                )
            }
            R.id.action_add -> {
                viewModel.updateSelectedNotes(NoteModel())
                activity?.invalidateOptionsMenu()
                findNavController(this).navigateSafe(
                    R.id.noteListingFragment,
                    R.id.action_noteListingFragment_to_addNewNoteFragment
                )
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
