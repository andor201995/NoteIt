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
        }
        setUpOnClickListType()
        logout_btn.setOnClickListener {
            findNavController(this).navigate(R.id.action_settingFragment_to_confirmationFragment)
        }
    }

    private fun setUpOnClickListType() {
        setting_view_type_linear.setOnClickListener {
            viewModel.changeListTypeTo(ListingType.Linear)
            clearButtonBackGround()
            it.background = ContextCompat.getDrawable(context!!, R.drawable.backgound_button_select)
        }
        setting_view_type_grid.setOnClickListener {
            viewModel.changeListTypeTo(ListingType.Grid)
            clearButtonBackGround()
            it.background = ContextCompat.getDrawable(context!!, R.drawable.backgound_button_select)
        }
        setting_view_type_staggered.setOnClickListener {
            viewModel.changeListTypeTo(ListingType.Staggered)
            clearButtonBackGround()
            it.background = ContextCompat.getDrawable(context!!, R.drawable.backgound_button_select)
        }
    }

    private fun clearButtonBackGround() {
        setting_view_type_linear.setBackgroundColor(Color.TRANSPARENT)
        setting_view_type_grid.setBackgroundColor(Color.TRANSPARENT)
        setting_view_type_staggered.setBackgroundColor(Color.TRANSPARENT)
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
        val bottomSheet = dialog!!.findViewById(com.google.android.material.R.id.design_bottom_sheet) as FrameLayout
        val bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet)
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
    }
}
