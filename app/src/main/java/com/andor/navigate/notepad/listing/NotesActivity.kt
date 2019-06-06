package com.andor.navigate.notepad.listing

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import com.andor.navigate.notepad.R
import com.andor.navigate.notepad.core.NoteViewModel
import com.andor.navigate.notepad.core.NoteViewModelFactory
import kotlinx.android.synthetic.main.activity_listing.*
import kotlinx.android.synthetic.main.content_main.*

class NotesActivity : AppCompatActivity() {
    private lateinit var viewModel: NoteViewModel

    companion object {
        fun intent(context: Context): Intent {
            return Intent(context, NotesActivity::class.java)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_listing)
        setSupportActionBar(toolbar)
        val appBarConfiguration = AppBarConfiguration
            .Builder(
                R.id.noteListingFragment
            )
            .build()
        NavigationUI.setupActionBarWithNavController(
            this,
            NavHostFragment.findNavController(nav_host),
            appBarConfiguration
        )

        viewModel =
            ViewModelProviders.of(this, NoteViewModelFactory(application!!, uid = intent!!.getStringExtra("uid")!!))
                .get(NoteViewModel::class.java)
    }

    override fun onSupportNavigateUp(): Boolean = Navigation.findNavController(
        this,
        R.id.nav_host
    ).navigateUp()

    fun setActionBarTitle(title: String) {
        toolbar.title = title
    }
}
