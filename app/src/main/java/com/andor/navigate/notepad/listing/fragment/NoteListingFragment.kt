package com.andor.navigate.notepad.listing.fragment


import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageButton
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.andor.navigate.notepad.R
import com.andor.navigate.notepad.listing.adapter.ListItemEvent
import com.andor.navigate.notepad.listing.adapter.ListingAdapter
import kotlinx.android.synthetic.main.fragment_note_listing.*


class NoteListingFragment : Fragment() {
    private var longPressActionMode: ActionMode? = null
    private var isLongPressed: Boolean = false
    private val selectedNotes: HashSet<String> = HashSet()
    private lateinit var viewModel: NoteViewModel

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(activity!!).get(NoteViewModel::class.java)
        hideKeyBoard()
        setAddNoteClickEvent()
        setUpListAdapter()
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        setHasOptionsMenu(true)
        return inflater.inflate(R.layout.fragment_note_listing, container, false)
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        if (isLongPressed) {
            val actionModeCallack = object : ActionMode.Callback {
                override fun onActionItemClicked(actionBarMenuItems: ActionMode?, menuItem: MenuItem): Boolean {
                    return onOptionsItemSelected(menuItem)
                }

                override fun onCreateActionMode(actionBarMenuItems: ActionMode?, menu: Menu?): Boolean {
                    this@NoteListingFragment.longPressActionMode = actionBarMenuItems
                    actionBarMenuItems?.menuInflater?.inflate(R.menu.menu_item_long_press, menu)
                    return true
                }

                override fun onPrepareActionMode(actionBarMenuItems: ActionMode?, menu: Menu?): Boolean {
                    return true
                }

                override fun onDestroyActionMode(actionBarMenuItems: ActionMode?) {
                    this@NoteListingFragment.longPressActionMode = null
                    selectedNotes.clear()
                    isLongPressed = false
                    setUpListAdapter()
                }
            }
            if (longPressActionMode == null) {
                activity!!.startActionMode(actionModeCallack)
            }
        }
    }

    private fun setAddNoteClickEvent() {
        addNoteButton.setOnClickListener {
            val dialog = Dialog(context!!)
            dialog.setContentView(R.layout.new_note_dialog)
            dialog.setTitle(R.string.new_note_hint)

            val newNoteImageButtonAccpt = dialog.findViewById<ImageButton>(R.id.newNoteButtonAccept)
            val newNoteImageButtonReject = dialog.findViewById<ImageButton>(R.id.newNoteButtonCancel)

            newNoteImageButtonAccpt.setOnClickListener {
                val newHeadText = dialog.findViewById<EditText>(R.id.newNoteHeadText).text.toString()

                val action = if (newHeadText.isBlank()) {
                    NoteListingFragmentDirections.actionNoteListingFragmentToAddNoteFragment()
                } else {
                    NoteListingFragmentDirections.actionNoteListingFragmentToAddNoteFragment(
                        newHeadText
                    )
                }
                dialog.cancel()
                Navigation.findNavController(view!!).navigate(action)
            }
            newNoteImageButtonReject.setOnClickListener {
                dialog.cancel()
            }
            dialog.show()
        }
    }

    private fun hideKeyBoard() {
        val imm = activity!!.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view!!.windowToken, 0)
    }

    private fun setUpListAdapter() {
        viewModel.allNotes.observe(this, Observer { notes ->
            notes.let { note ->
                val listingAdapter = ListingAdapter(context!!, note) {
                    when (it) {
                        is ListItemEvent.SingleClickEvent -> {
                            if (!isLongPressed) {
                                val action =
                                    NoteListingFragmentDirections.actionNoteListingFragmentToExpandedNoteFragment(
                                        it.noteModel.noteHead,
                                        it.noteModel.noteBody
                                    )
                                Navigation.findNavController(view!!).navigate(action)
                            } else {
                                if (selectedNotes.contains(it.noteModel.noteHead)) {
                                    selectedNotes.remove(it.noteModel.noteHead)
                                    if (selectedNotes.size == 0) {
                                        longPressActionMode?.finish()
                                    }
                                } else {
                                    selectedNotes.add(it.noteModel.noteHead)
                                }
                            }
                        }
                        is ListItemEvent.LongClickEvent -> {
                            isLongPressed = true
                            selectedNotes.add(it.noteModel.noteHead)
                            activity!!.invalidateOptionsMenu()
                        }
                    }
                }
                listRecyclerView.addItemDecoration(DividerItemDecoration(context, LinearLayoutManager.VERTICAL))
                val linearLayoutManager = LinearLayoutManager(context)
                linearLayoutManager.orientation = RecyclerView.VERTICAL
                listRecyclerView.layoutManager = linearLayoutManager
                listRecyclerView.adapter = listingAdapter
            }
        })
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item!!.itemId == R.id.SelectedItemDelete) {
            viewModel.delete(HashSet(selectedNotes))
            longPressActionMode!!.finish()
        }
        return super.onOptionsItemSelected(item)
    }
}
