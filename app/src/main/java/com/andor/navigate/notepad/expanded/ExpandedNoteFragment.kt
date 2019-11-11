package com.andor.navigate.notepad.expanded


import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.andor.navigate.notepad.R
import com.andor.navigate.notepad.core.NoteViewModel
import com.andor.navigate.notepad.core.navigateSafe
import com.andor.navigate.notepad.listing.NotesActivity
import kotlinx.android.synthetic.main.fragment_expanded_note.*


class ExpandedNoteFragment : Fragment() {

    private lateinit var viewModel: NoteViewModel

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel = ViewModelProvider(activity!!).get(NoteViewModel::class.java)

        val viewPagerAdapter = ViewPagerAdapter(context!!) {
            if (it is ViewPageItemEvent.DoubleClickEvent) {
                if (it.adapterPosition >= 0) {
                    openEditor()
                }
            }
        }
        val value = viewModel.getAppStateStream().value!!
        val listOfAllNotes = value.listOfAllNotes
        viewPagerAdapter.pageList = listOfAllNotes
        expanded_view_pager.adapter = viewPagerAdapter
        expanded_view_pager.setCurrentItem(listOfAllNotes.indexOf(value.selectedNote), false)

        expanded_view_pager.registerOnPageChangeCallback(object :
            ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                viewModel.updateSelectedNotes(viewModel.getAppStateStream().value!!.listOfAllNotes[position])
            }
        })


        viewModel.getAppStateStream().observe(viewLifecycleOwner, Observer { appState ->
            appState.selectedNote?.let {
                (activity as NotesActivity).setActionBarTitle(it.head)
            }
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

    private fun openEditor() {
        val action =
            ExpandedNoteFragmentDirections.actionExpandedNoteFragmentToUpdateNoteFragment(true)
        findNavController(this).navigateSafe(R.id.expandedNoteFragment, action)
    }

}
