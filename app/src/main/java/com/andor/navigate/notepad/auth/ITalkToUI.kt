package com.andor.navigate.notepad.auth

import com.google.firebase.auth.FirebaseUser

interface ITalkToUI {
    fun signingInSuccess(user: FirebaseUser)
    fun signingInFailed()
    fun alreadySignedIn(user: FirebaseUser)

}
