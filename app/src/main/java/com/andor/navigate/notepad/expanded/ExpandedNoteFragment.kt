package com.andor.navigate.notepad.expanded


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.andor.navigate.notepad.MainActivity
import kotlinx.android.synthetic.main.fragment_expanded_note.*


class ExpandedNoteFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        val headText = ExpandedNoteFragmentArgs.fromBundle(arguments!!).headText

        (activity as MainActivity).setActionBarTitle(headText)

        return inflater.inflate(com.andor.navigate.notepad.R.layout.fragment_expanded_note, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val bodyText = ExpandedNoteFragmentArgs.fromBundle(arguments!!).bodyText
        expandedNoteTxt.text = bodyText
    }
}
