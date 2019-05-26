package com.andor.navigate.notepad.listing


import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageButton
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.andor.navigate.notepad.R
import kotlinx.android.synthetic.main.fragment_note_listing.*


class NoteListingFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_note_listing, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        hideKeyBoard()
        setAddNoteClickEvent()
        setUpListAdapter()
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
                    NoteListingFragmentDirections.actionNoteListingFragmentToAddNoteFragment(newHeadText)
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
        val contentList = NoteRepoImpl.getNotes()
        val listingAdapter = ListingAdapter(context!!, contentList)
        listRecyclerView.addItemDecoration(DividerItemDecoration(context, LinearLayoutManager.VERTICAL))
        val linearLayoutManager = LinearLayoutManager(context)
        linearLayoutManager.orientation = RecyclerView.VERTICAL
        listRecyclerView.layoutManager = linearLayoutManager
        listRecyclerView.adapter = listingAdapter
    }


}
