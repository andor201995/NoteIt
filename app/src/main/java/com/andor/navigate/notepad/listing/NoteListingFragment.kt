package com.andor.navigate.notepad.listing


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.andor.navigate.notepad.R
import kotlinx.android.synthetic.main.fragment_note_listing.*


class NoteListingFragment : Fragment() {

    private lateinit var noteRepo: NoteRepo

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        noteRepo = NoteRepoImpl()
        return inflater.inflate(R.layout.fragment_note_listing, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpListAdapter()
    }

    private fun setUpListAdapter() {
        val contentList = noteRepo.getNotes()
        val listingAdapter = ListingAdapter(context!!, contentList)
        listRecyclerView.addItemDecoration(DividerItemDecoration(context, LinearLayoutManager.VERTICAL))
        val linearLayoutManager = LinearLayoutManager(context)
        linearLayoutManager.orientation = RecyclerView.VERTICAL
        listRecyclerView.layoutManager = linearLayoutManager
        listRecyclerView.adapter = listingAdapter
    }


}
