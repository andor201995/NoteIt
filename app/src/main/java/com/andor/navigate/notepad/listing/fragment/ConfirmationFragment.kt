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


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 *
 */
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
