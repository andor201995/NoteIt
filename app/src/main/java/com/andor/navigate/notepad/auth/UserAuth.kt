package com.andor.navigate.notepad.auth

import android.app.Activity
import android.app.Dialog
import android.content.Context
import com.google.firebase.auth.AuthCredential

interface UserAuth {
    fun isUserSignedIn(context: Context)
    fun signInFireBaseUser(email: String, password: String, activity: Activity)
    fun signUpFireBaseUser(
        email: String,
        password: String, displayName: String,
        activity: Activity,
        dialog: Dialog
    )

    fun signInGoogleUser(credential: AuthCredential, activity: Activity)
}
