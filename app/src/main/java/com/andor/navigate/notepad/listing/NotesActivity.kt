package com.andor.navigate.notepad.listing

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import com.andor.navigate.notepad.R
import com.andor.navigate.notepad.listing.fragment.NoteViewModel
import kotlinx.android.synthetic.main.activity_listing.*
import kotlinx.android.synthetic.main.content_main.*

class NotesActivity : AppCompatActivity() {
    private lateinit var viewModel: NoteViewModel

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
        viewModel = ViewModelProviders.of(this).get(NoteViewModel::class.java)
        viewModel.uid = intent?.getStringExtra("uid")!!
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onSupportNavigateUp(): Boolean = Navigation.findNavController(
        this,
        R.id.nav_host
    ).navigateUp()

    fun setActionBarTitle(title: String) {
        toolbar.title = title
    }
}
