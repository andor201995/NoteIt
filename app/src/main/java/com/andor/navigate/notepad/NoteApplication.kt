package com.andor.navigate.notepad

import android.app.Application
import com.andor.navigate.notepad.auth.ITalkToUI
import com.andor.navigate.notepad.auth.UserAuth
import com.andor.navigate.notepad.auth.UserAuthentication
import com.andor.navigate.notepad.core.NoteViewModel
import com.andor.navigate.notepad.listing.dao.NoteRepoImpl
import com.andor.navigate.notepad.listing.fragment.BottomSheetMenuFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.dsl.module

class NoteApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        val noteModule = module(override = true) {
            single { BottomSheetMenuFragment() }
            single { (id: String) -> NoteRepoImpl(id, get()) }
            factory { (iTalkToUI: ITalkToUI) -> UserAuthentication(iTalkToUI, get(), get()) as UserAuth }
            single { FirebaseFirestore.getInstance() }
            single { FirebaseAuth.getInstance() }
            viewModel { NoteViewModel(androidApplication(), get()) }
        }

        startKoin {
            androidContext(this@NoteApplication)
            modules(noteModule)
        }
    }
}