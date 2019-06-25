package com.andor.navigate.notepad.listing.fragment


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.NavHostFragment.findNavController
import com.andor.navigate.notepad.R
import com.andor.navigate.notepad.core.BottomMenuType
import com.andor.navigate.notepad.core.NoteViewModel


class NavMenuFragment : Fragment() {

    private lateinit var viewModel: NoteViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_nav_menu, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(activity!!).get(NoteViewModel::class.java)
    }

    override fun onStart() {
        super.onStart()
        val navController = findNavController(this)
        when (viewModel.getAppStateStream().value!!.bottomMenuType) {
            is BottomMenuType.AddNote -> {
                navController.navigate(R.id.action_navMenuFragment_to_addNewNoteFragment)
            }
            is BottomMenuType.Setting -> {
                navController.navigate(R.id.action_navMenuFragment_to_settingFragment)
            }
        }
    }
}
