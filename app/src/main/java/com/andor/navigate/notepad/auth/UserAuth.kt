package com.andor.navigate.notepad.auth

import android.app.Activity
import android.app.Dialog
import android.content.Context

interface UserAuth {
    fun isUserSignedIn(context: Context)
    fun signInFireBaseUser(email: String, password: String, activity: Activity)
    fun signUpFireBaseUser(
        email: String,
        password: String,
        activity: Activity,
        dialog: Dialog
    )
}
