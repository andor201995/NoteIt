package com.andor.navigate.notepad.auth

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.util.Log
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.firestore.FirebaseFirestore

class UserAuthentication(private val iTalkToUI: ITalkToUI) : UserAuth {
    private val fireBaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private val fireBaseFireStore = FirebaseFirestore.getInstance()

    companion object {
        const val LOGOUT: String = "LOGOUT"
        const val TAG: String = "AUTH"
    }

    override fun signInGoogleUser(credential: AuthCredential, activity: Activity) {
        fireBaseAuth.signInWithCredential(credential)
            .addOnCompleteListener(activity) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "signInWithCredential:success")
                    val user = fireBaseAuth.currentUser
                    createUserIfNeeded(user!!)
                    iTalkToUI.signingInSuccess(user)
                } else {
                    Log.w(TAG, "signInWithCredential:failure", task.exception)
                    iTalkToUI.signingInFailed()
                }
            }
    }


    override fun signUpFireBaseUser(
        email: String,
        password: String, displayName: String,
        activity: Activity,
        dialog: Dialog
    ) {
        fireBaseAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(activity) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "createUserWithEmail:success")
                    val user = fireBaseAuth.currentUser
                    val profileBuilder = UserProfileChangeRequest.Builder()
                    profileBuilder.setDisplayName(displayName)
                    user!!.updateProfile(profileBuilder.build())
                    dialog.cancel()
                    createUserIfNeeded(user)
                    iTalkToUI.signingInSuccess(user)
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "createUserWithEmail:failure", task.exception)
                    iTalkToUI.signingInFailed()
                }
            }
    }

    private fun createUserIfNeeded(user: FirebaseUser) {
        fireBaseFireStore.collection("Users").document(user.uid).get().addOnCompleteListener {
            if (it.isSuccessful && !it.result!!.exists()) {
                val defaultData = hashMapOf("head" to "Hello", "body" to "Welcome to NoteIt...")
                fireBaseFireStore.collection("Users").document(user.uid).collection("Notes").document("Default")
                    .set(defaultData)
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
                        iTalkToUI.signingInSuccess(user!!)
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "signInWithEmail:failure", task.exception)
                        iTalkToUI.signingInFailed()
                    }
                }
        }
    }


    override fun isUserSignedIn(context: Context) {
        //Fire-base User
        val user = fireBaseAuth.currentUser
        if (user != null) {
            iTalkToUI.alreadySignedIn(user)
        }
    }

    override fun logout() {
        fireBaseAuth.signOut()
    }
}