package com.andor.navigate.notepad.listing.fragment


import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.andor.navigate.notepad.R
import com.andor.navigate.notepad.auth.AuthActivity
import com.andor.navigate.notepad.auth.UserAuthentication
import com.andor.navigate.notepad.core.NoteViewModel

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
