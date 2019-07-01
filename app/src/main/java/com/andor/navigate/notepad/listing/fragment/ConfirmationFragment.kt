package com.andor.navigate.notepad.listing.fragment


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import com.andor.navigate.notepad.R
import com.andor.navigate.notepad.auth.AuthActivity
import com.andor.navigate.notepad.auth.UserAuthentication
import kotlinx.android.synthetic.main.fragment_confirmation.*


class ConfirmationFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_confirmation, container, false)
    }

    override fun onStart() {
        super.onStart()
        button_logout_no.setOnClickListener {
            NavHostFragment.findNavController(this).navigateUp()
        }
        button_logout_yes.setOnClickListener {
            logOut()
        }
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
