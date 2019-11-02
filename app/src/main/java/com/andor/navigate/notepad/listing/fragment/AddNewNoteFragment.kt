package com.andor.navigate.notepad.listing.fragment


import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.FrameLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment.findNavController
import com.andor.navigate.notepad.R
import com.andor.navigate.notepad.core.*
import com.andor.navigate.notepad.listing.dao.NoteModel
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.fragment_add_new_note.*
import java.util.*


class AddNewNoteFragment : BottomSheetDialogFragment() {


    private lateinit var viewModel: NoteViewModel
    private var selectedBGType: String = NoteModel.NOTE_BG0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_new_note, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(activity!!).get(NoteViewModel::class.java)
        viewModel.getAppStateStream().value!!.let {

            val args = AddNewNoteFragmentArgs.fromBundle(arguments!!)
            if (!args.isNewNote) {
                view!!.background = Utils.getBackGroundRes(context!!, it.selectedNote!!.bg)
                selectedBGType = it.selectedNote.bg
                newNoteHeadText.setText(it.selectedNote.head, TextView.BufferType.EDITABLE)
                oldNoteUpdateButton.visibility = View.VISIBLE
                newNoteButtonAccept.visibility = View.GONE
            } else {
                oldNoteUpdateButton.visibility = View.GONE
                newNoteButtonAccept.visibility = View.VISIBLE
            }
            setButtonClickListener()
        }

        viewModel.getAppEventStream().observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            handleEvent(it.getContentIfNotHandled())
        })

        viewModel.getAppAlertStream().observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            handleAlert(it.getContentIfNotHandled())
        })
    }

    private fun handleAlert(alertEvent: AlertEvent?) {
        when (alertEvent) {
            is AlertEvent.TitleEmptyToast -> Toast.makeText(
                context,
                R.string.empty_title_toast,
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun handleEvent(event: EventOnFragment?) {
        when (event) {
            is EventOnFragment.AddNoteEvent.AddNote -> findNavController(this).navigateSafe(
                R.id.addNewNoteFragment,
                R.id.action_addNewNoteFragment_to_updateNoteFragment
            )
            is EventOnFragment.AddNoteEvent.UpdateNote -> findNavController(this).navigateUp()
            is EventOnFragment.AddNoteEvent.Cancel -> findNavController(this).navigateUp()
        }
    }

    private fun setButtonClickListener() {
        newNoteButtonAccept.setOnClickListener {
            val noteModel = NoteModel(
                id = UUID.randomUUID().toString(),
                head = newNoteHeadText.text.toString(),
                bg = selectedBGType
            )
            viewModel.handleFragmentEvent(EventOnFragment.AddNoteEvent.AddNote(noteModel))

        }
        newNoteButtonCancel.setOnClickListener {
            viewModel.handleFragmentEvent(EventOnFragment.AddNoteEvent.Cancel)
        }

        oldNoteUpdateButton.setOnClickListener {
            val noteModel = viewModel.getAppStateStream().value!!.selectedNote!!
            viewModel.handleFragmentEvent(
                EventOnFragment.AddNoteEvent.UpdateNote(
                    noteModel.copy(
                        head = newNoteHeadText.text.toString(), bg = selectedBGType
                    )
                )
            )
        }
        setNoteBackGroundButtonListener()
    }

    private fun setNoteBackGroundButtonListener() {
        btn_bg_0.setOnClickListener {
            clearButtonBackGround()
            btn_bg_0.background =
                ContextCompat.getDrawable(context!!, R.drawable.backgound_button_select)
            selectedBGType = NoteModel.NOTE_BG0
            addFragmentContainer.background =
                ContextCompat.getDrawable(context!!, R.drawable.background_note_0)
        }
        btn_bg_1.setOnClickListener {
            clearButtonBackGround()
            btn_bg_1.background =
                ContextCompat.getDrawable(context!!, R.drawable.backgound_button_select)
            selectedBGType = NoteModel.NOTE_BG1
            addFragmentContainer.background =
                ContextCompat.getDrawable(context!!, R.drawable.background_note_1)
        }
        btn_bg_2.setOnClickListener {
            clearButtonBackGround()
            btn_bg_2.background =
                ContextCompat.getDrawable(context!!, R.drawable.backgound_button_select)
            selectedBGType = NoteModel.NOTE_BG2
            addFragmentContainer.background =
                ContextCompat.getDrawable(context!!, R.drawable.background_note_2)
        }
        btn_bg_3.setOnClickListener {
            clearButtonBackGround()
            btn_bg_3.background =
                ContextCompat.getDrawable(context!!, R.drawable.backgound_button_select)
            selectedBGType = NoteModel.NOTE_BG3
            addFragmentContainer.background =
                ContextCompat.getDrawable(context!!, R.drawable.background_note_3)
        }
        btn_bg_4.setOnClickListener {
            clearButtonBackGround()
            btn_bg_4.background =
                ContextCompat.getDrawable(context!!, R.drawable.backgound_button_select)
            selectedBGType = NoteModel.NOTE_BG4
            addFragmentContainer.background =
                ContextCompat.getDrawable(context!!, R.drawable.background_note_4)
        }
        btn_bg_5.setOnClickListener {
            clearButtonBackGround()
            btn_bg_5.background =
                ContextCompat.getDrawable(context!!, R.drawable.backgound_button_select)
            selectedBGType = NoteModel.NOTE_BG5
            addFragmentContainer.background =
                ContextCompat.getDrawable(context!!, R.drawable.background_note_5)
        }

    }

    private fun clearButtonBackGround() {
        btn_bg_0.setBackgroundColor(Color.TRANSPARENT)
        btn_bg_1.setBackgroundColor(Color.TRANSPARENT)
        btn_bg_2.setBackgroundColor(Color.TRANSPARENT)
        btn_bg_3.setBackgroundColor(Color.TRANSPARENT)
        btn_bg_4.setBackgroundColor(Color.TRANSPARENT)
        btn_bg_5.setBackgroundColor(Color.TRANSPARENT)
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
        val bottomSheet =
            dialog!!.findViewById(com.google.android.material.R.id.design_bottom_sheet) as FrameLayout
        val bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet)
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
    }
}
