package com.andor.navigate.notepad.auth


import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.os.SystemClock
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import com.andor.navigate.notepad.R
import com.andor.navigate.notepad.listing.fragment.NoteViewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import kotlinx.android.synthetic.main.fragment_auth.*
import java.util.*


class AuthFragment : Fragment(), ITalkToUI {
    private lateinit var mUserAuth: UserAuth
    private lateinit var mGoogleSignInClient: GoogleSignInClient
    private lateinit var viewModel: NoteViewModel

    private val tagGoogleSignIn = 2121

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build()
        mGoogleSignInClient = GoogleSignIn.getClient(activity!!, gso)
        mUserAuth = UserAuthentication(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_auth, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(activity!!).get(NoteViewModel::class.java)
        hideProgressBar()
        mUserAuth.isUserSignedIn(context!!)

        btn_login.setOnClickListener(object : DebouncedOnClickListener(1000) {
            override fun onDebouncedClick(v: View) {
                signInUser(input_email.text.toString(), input_password.text.toString())
            }

        })

        signUpByMailButtonSetup()
        btn_google_login.setOnClickListener {
            val signInIntent = mGoogleSignInClient.signInIntent
            startActivityForResult(signInIntent, tagGoogleSignIn)
        }

    }

    private fun signUpByMailButtonSetup() {
        link_signup.setOnClickListener {
            val dialog = Dialog(context!!)
            dialog.setContentView(R.layout.create_user_dialog)

            val createAccountBtn = dialog.findViewById<AppCompatButton>(R.id.btn_create)
            val inputEmail = dialog.findViewById<EditText>(R.id.input_create_email)
            val inputPass = dialog.findViewById<EditText>(R.id.input_create_password)

            createAccountBtn.setOnClickListener(object : DebouncedOnClickListener(1000) {
                override fun onDebouncedClick(v: View) {
                    registerUser(
                        inputEmail.text.toString().trim(),
                        inputPass.text.toString().trim(),
                        dialog
                    )
                }

            })
            dialog.show()
        }
    }


    private fun signInUser(email: String, password: String) {
        if (isCredentialEmpty(email, password)) return
        showProgressBar()
        mUserAuth.signInFireBaseUser(email, password, activity!!)

    }

    private fun isCredentialEmpty(email: String, password: String): Boolean {
        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(context, "Email or Password is empty", Toast.LENGTH_SHORT).show()
            return true
        }
        return false
    }

    private fun registerUser(email: String, password: String, dialog: Dialog) {
        if (isCredentialEmpty(email, password)) return
        showProgressBar()
        mUserAuth.signUpFireBaseUser(email, password, activity!!, dialog)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == tagGoogleSignIn) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleSignInResult(task)
        }
    }

    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account = completedTask.getResult(ApiException::class.java)
            signingInSuccess(UserModel(account!!.displayName))
        } catch (e: ApiException) {
            Log.w(UserAuthentication.TAG, "signInResult:failed code=" + e.statusCode)
        }
    }

    override fun signingInSuccess(userModel: UserModel) {
        hideProgressBar()
        Navigation.findNavController(view!!).navigate(R.id.action_authFragment_to_noteListingFragment)
    }

    override fun signingInFailed() {
        hideProgressBar()
        Toast.makeText(
            activity, "Authentication failed.",
            Toast.LENGTH_SHORT
        ).show()
    }

    private fun showProgressBar() = progressBar.show()
    private fun hideProgressBar() = progressBar.hide()


}

abstract class DebouncedOnClickListener
    (private val minimumInterval: Long) : View.OnClickListener {
    private val lastClickMap: MutableMap<View, Long>
    abstract fun onDebouncedClick(v: View)

    init {
        this.lastClickMap = WeakHashMap()
    }

    override fun onClick(clickedView: View) {
        val previousClickTimestamp = lastClickMap[clickedView]
        val currentTimestamp = SystemClock.uptimeMillis()

        lastClickMap[clickedView] = currentTimestamp
        if (previousClickTimestamp == null || Math.abs(currentTimestamp - previousClickTimestamp.toLong()) > minimumInterval) {
            onDebouncedClick(clickedView)
        }
    }
}

