package com.andor.navigate.notepad.listing.fragment


import android.graphics.Color
import android.os.Bundle
import android.view.*
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.andor.navigate.notepad.R
import com.andor.navigate.notepad.auth.AuthActivity
import com.andor.navigate.notepad.auth.UserAuthentication
import com.andor.navigate.notepad.core.ListingType
import com.andor.navigate.notepad.core.NoteViewModel
import kotlinx.android.synthetic.main.fragment_setting.*

class SettingFragment : Fragment() {

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
        viewModel.appStateRelay.value!!.let {
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

    }

    private fun setUpOnClickListType() {
        setting_view_type_linear.setOnClickListener {
            viewModel.appStateRelay.postValue(viewModel.appStateRelay.value!!.copy(listingType = ListingType.Linear))
            clearButtonBackGround()
            it.background = ContextCompat.getDrawable(context!!, R.drawable.backgound_button_select)
        }
        setting_view_type_grid.setOnClickListener {
            viewModel.appStateRelay.postValue(viewModel.appStateRelay.value!!.copy(listingType = ListingType.Grid))
            clearButtonBackGround()
            it.background = ContextCompat.getDrawable(context!!, R.drawable.backgound_button_select)
        }
        setting_view_type_staggered.setOnClickListener {
            viewModel.appStateRelay.postValue(viewModel.appStateRelay.value!!.copy(listingType = ListingType.Staggered))
            clearButtonBackGround()
            it.background = ContextCompat.getDrawable(context!!, R.drawable.backgound_button_select)
        }
    }

    private fun clearButtonBackGround() {
        setting_view_type_linear.setBackgroundColor(Color.WHITE)
        setting_view_type_grid.setBackgroundColor(Color.WHITE)
        setting_view_type_staggered.setBackgroundColor(Color.WHITE)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.setting, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.action_logout) {
            logOut()
        }
        return super.onOptionsItemSelected(item)
    }


    private fun logOut() {
        context?.let {
            val intent = AuthActivity.intent(it)
            intent.putExtra(UserAuthentication.LOGOUT, true)
            startActivity(intent)
            activity!!.finish()
        }
    }

}
