package com.andor.navigate.notepad.core

import com.andor.navigate.notepad.listing.dao.NoteRepoImpl
import com.andor.navigate.notepad.listing.fragment.BottomSheetMenuFragment
import org.koin.dsl.module

val noteModule = module {
    single { BottomSheetMenuFragment() }
    single { (id: String) -> NoteRepoImpl(id) }
}
