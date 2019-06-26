package com.andor.navigate.notepad

import android.app.Application
import com.andor.navigate.notepad.core.noteModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class NoteApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@NoteApplication)
            modules(noteModule)
        }
    }
}