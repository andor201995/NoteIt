package com.andor.navigate.notepad.auth

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.util.Log
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.firebase.auth.FirebaseAuth

class UserAuthentication(private val iTalkToUI: ITalkToUI) : UserAuth {
    override fun signUpFireBaseUser(
        email: String,
        password: String,
        activity: Activity,
        dialog: Dialog
    ) {
        fireBaseAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(activity) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "createUserWithEmail:success")
                    val user = fireBaseAuth.currentUser
                    dialog.cancel()
                    iTalkToUI.signingInSuccess(UserModel(user!!.displayName))
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "createUserWithEmail:failure", task.exception)
                    iTalkToUI.signingInFailed()
                }
            }
    }


    override fun signInFireBaseUser(email: String, password: String, activity: Activity) {
        if (fireBaseAuth.currentUser == null) {
            fireBaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(activity) { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG, "signInWithEmail:success")
                        val user = fireBaseAuth.currentUser
                        iTalkToUI.signingInSuccess(UserModel(user!!.displayName))
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "signInWithEmail:failure", task.exception)
                        iTalkToUI.signingInFailed()
                    }
                }
        }
    }

    companion object {
        val fireBaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
        const val TAG: String = "AUTH"
    }


    override fun isUserSignedIn(context: Context) {
        //Google User
        val googleUser = GoogleSignIn.getLastSignedInAccount(context)
        //Fire-base User
        val fireBaseUser = fireBaseAuth.currentUser

        if (googleUser != null) {
            iTalkToUI.signingInSuccess(UserModel(googleUser.displayName))
        } else if (fireBaseUser != null) {
            iTalkToUI.signingInSuccess(UserModel(fireBaseUser.displayName))
        }
    }

}

data class UserModel(private val name: String?)