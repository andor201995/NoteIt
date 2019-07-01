package com.andor.navigate.notepad.auth

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import com.andor.navigate.notepad.R
import com.andor.navigate.notepad.core.DebouncedOnClickListener
import com.andor.navigate.notepad.listing.NotesActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.android.synthetic.main.activity_auth.*
import org.koin.android.ext.android.inject
import org.koin.core.parameter.parametersOf

class AuthActivity : AppCompatActivity(), ITalkToUI {
    private val mUserAuth: UserAuth by inject { parametersOf(this as ITalkToUI) }
    private lateinit var mGoogleSignInClient: GoogleSignInClient

    private val tagGoogleSignIn = 2121


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.request_id_token))
            .requestEmail()
            .build()

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso)
        val logout = intent?.getBooleanExtra(UserAuthentication.LOGOUT, false)!!
        if (!logout) {
            mUserAuth.isUserSignedIn(this)
        } else {
            mUserAuth.logout()
        }

        hideProgressBar()

        //set the listener for login button
        btn_login.setOnClickListener(object : DebouncedOnClickListener(1000) {
            override fun onDebouncedClick(v: View) {
                signInUser(input_email.text.toString(), input_password.text.toString())
            }

        })

        //set signUP button
        signUpByMailButtonSetup()

        //set google signIn/Up button
        btn_google_login.setOnClickListener {
            val signInIntent = mGoogleSignInClient.signInIntent
            progressBar.show()
            startActivityForResult(signInIntent, tagGoogleSignIn)
        }
    }


    private fun signUpByMailButtonSetup() {
        link_signup.setOnClickListener {
            val dialog = Dialog(this)
            dialog.setContentView(R.layout.create_user_dialog)

            val createAccountBtn = dialog.findViewById<AppCompatButton>(R.id.btn_create)
            val inputEmail = dialog.findViewById<EditText>(R.id.input_create_email)
            val inputPass = dialog.findViewById<EditText>(R.id.input_create_password)
            val inputName = dialog.findViewById<EditText>(R.id.input_create_name)

            createAccountBtn.setOnClickListener(object : DebouncedOnClickListener(1000) {
                override fun onDebouncedClick(v: View) {
                    dialog.cancel()
                    registerUser(
                        inputEmail.text.toString().trim(),
                        inputPass.text.toString().trim(),
                        inputName.text.toString().trim()
                    )
                }

            })
            dialog.show()
        }
    }


    private fun signInUser(email: String, password: String) {
        if (isCredentialEmpty(email, password)) return
        showProgressBar()
        mUserAuth.signInFireBaseUser(email, password, this)
    }

    private fun isCredentialEmpty(email: String, password: String): Boolean {
        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Email or Password is empty", Toast.LENGTH_SHORT).show()
            return true
        }
        return false
    }

    private fun registerUser(email: String, password: String, name: String) {
        if (isCredentialEmpty(email, password)) return
        showProgressBar()
        mUserAuth.signUpFireBaseUser(email, password, name, this)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == tagGoogleSignIn) {
            progressBar.hide()
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleSignInResult(task)
        }
    }

    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account = completedTask.getResult(ApiException::class.java)
            val credential = GoogleAuthProvider.getCredential(account!!.idToken, null)
            mUserAuth.signInGoogleUser(credential, this)
        } catch (e: ApiException) {
            Log.w(UserAuthentication.TAG, "signInResult:failed code=" + e.statusCode)
        }
    }

    override fun signingInSuccess(user: FirebaseUser) {
        hideProgressBar()
        startListingActivityForSignedInUser(user)
    }

    override fun signingInFailed() {
        hideProgressBar()
        Toast.makeText(
            this, "Authentication failed.",
            Toast.LENGTH_SHORT
        ).show()
    }

    override fun alreadySignedIn(user: FirebaseUser) {
        startListingActivityForSignedInUser(user)
    }

    private fun startListingActivityForSignedInUser(user: FirebaseUser) {
        val intent = NotesActivity.intent(this)
        intent.putExtra("uid", user.uid)
        startActivity(intent)
        finish()
    }

    private fun showProgressBar() = progressBar.show()
    private fun hideProgressBar() = progressBar.hide()

    companion object {
        fun intent(context: Context): Intent {
            return Intent(context, AuthActivity::class.java)
        }
    }


}
