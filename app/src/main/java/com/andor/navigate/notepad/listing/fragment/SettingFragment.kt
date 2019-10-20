package com.andor.navigate.notepad.listing.fragment


import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.FrameLayout
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.NavHostFragment.findNavController
import com.andor.navigate.notepad.R
import com.andor.navigate.notepad.core.ListingType
import com.andor.navigate.notepad.core.NoteViewModel
import com.andor.navigate.notepad.core.SortingType
import com.andor.navigate.notepad.core.navigateSafe
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.fragment_setting.*

class SettingFragment : BottomSheetDialogFragment() {

    private lateinit var viewModel: NoteViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_setting, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(activity!!).get(NoteViewModel::class.java)

        //init setting background
        viewModel.getAppStateStream().value!!.let {
            when (it.listingType) {
                is ListingType.Linear -> {
                    setting_view_type_linear.background =
                        ContextCompat.getDrawable(context!!, R.drawable.backgound_button_select)
                }
                is ListingType.Grid -> {
                    setting_view_type_grid.background =
                        ContextCompat.getDrawable(context!!, R.drawable.backgound_button_select)
                }
                is ListingType.Staggered -> {
                    setting_view_type_staggered.background =
                        ContextCompat.getDrawable(context!!, R.drawable.backgound_button_select)
                }
            }
            when (it.sortingType) {
                is SortingType.DateCreated -> {
                    sortByCreated.background =
                        ContextCompat.getDrawable(context!!, R.drawable.backgound_button_select)
                }
                is SortingType.DateUpdated -> {
                    sortByRecentChange.background =
                        ContextCompat.getDrawable(context!!, R.drawable.backgound_button_select)
                }
                is SortingType.Alphabet -> {
                    sortByAlphabet.background =
                        ContextCompat.getDrawable(context!!, R.drawable.backgound_button_select)
                }
            }
        }
        setUpOnClickListType()
        logout_btn.setOnClickListener {
            findNavController(this).navigateSafe(
                R.id.settingFragment,
                R.id.action_settingFragment_to_confirmationFragment
            )
        }
    }

    private fun setUpOnClickListType() {
        setting_view_type_linear.setOnClickListener {
            viewModel.changeListTypeTo(ListingType.Linear)
            clearListButtonBackGround()
            it.background = ContextCompat.getDrawable(context!!, R.drawable.backgound_button_select)
        }
        setting_view_type_grid.setOnClickListener {
            viewModel.changeListTypeTo(ListingType.Grid)
            clearListButtonBackGround()
            it.background = ContextCompat.getDrawable(context!!, R.drawable.backgound_button_select)
        }
        setting_view_type_staggered.setOnClickListener {
            viewModel.changeListTypeTo(ListingType.Staggered)
            clearListButtonBackGround()
            it.background = ContextCompat.getDrawable(context!!, R.drawable.backgound_button_select)
        }
        sortByAlphabet.setOnClickListener {
            viewModel.changeSortingType(SortingType.Alphabet)
            clearSortButtonBackGround()
            it.background = ContextCompat.getDrawable(context!!, R.drawable.backgound_button_select)
        }
        sortByRecentChange.setOnClickListener {
            viewModel.changeSortingType(SortingType.DateUpdated)
            clearSortButtonBackGround()
            it.background = ContextCompat.getDrawable(context!!, R.drawable.backgound_button_select)
        }
        sortByCreated.setOnClickListener {
            viewModel.changeSortingType(SortingType.DateCreated)
            clearSortButtonBackGround()
            it.background = ContextCompat.getDrawable(context!!, R.drawable.backgound_button_select)
        }

    }

    private fun clearSortButtonBackGround() {
        sortByCreated.setBackgroundColor(Color.TRANSPARENT)
        sortByRecentChange.setBackgroundColor(Color.TRANSPARENT)
        sortByAlphabet.setBackgroundColor(Color.TRANSPARENT)
    }

    private fun clearListButtonBackGround() {
        setting_view_type_linear.setBackgroundColor(Color.TRANSPARENT)
        setting_view_type_grid.setBackgroundColor(Color.TRANSPARENT)
        setting_view_type_staggered.setBackgroundColor(Color.TRANSPARENT)
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
