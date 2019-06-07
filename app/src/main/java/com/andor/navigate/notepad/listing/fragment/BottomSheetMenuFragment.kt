package com.andor.navigate.notepad.listing.fragment


import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import com.andor.navigate.demonavigation.fragment.BottomSheetNavFragment
import com.andor.navigate.notepad.R
import com.andor.navigate.notepad.core.BottomMenuEvent
import com.andor.navigate.notepad.core.NoteViewModel
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

open class BottomSheetMenuFragment : BottomSheetDialogFragment() {

    private lateinit var viewModel: NoteViewModel

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(activity!!).get(NoteViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_bottom_sheet_menu, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val bottomSheetNavFragment = BottomSheetNavFragment()
        childFragmentManager.beginTransaction()
            .replace(R.id.bottomMenuContainer, bottomSheetNavFragment, "Bottom_sheet_Fragment")
            .addToBackStack("Bottom_sheet_Fragment").commit()
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return object : BottomSheetDialog(activity!!, theme) {
            override fun onBackPressed() {
                val fragment = childFragmentManager.fragments[0]
                val navigateUp = Navigation.findNavController(fragment.view!!).navigateUp()
                if (!navigateUp) {
                    dismiss()
                }
            }

            override fun dismiss() {
                super.dismiss()
                viewModel.appStateRelay.postValue(viewModel.appStateRelay.value?.copy(bottomMenuEvent = BottomMenuEvent.Close))
            }
        }
    }


}
