package com.andor.navigate.notepad.listing

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment.findNavController
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
            findNavController(nav_host),
            appBarConfiguration
        )

        // Verify the action and get the query
        if (Intent.ACTION_SEARCH == intent.action) {
            intent.getStringExtra(SearchManager.QUERY)?.also { query ->
                doMySearch(query)
            }
        }
        viewModel =
            ViewModelProvider(
                this,
                NoteViewModelFactory(application!!, uid = intent!!.getStringExtra("uid")!!)
            )
                .get(NoteViewModel::class.java)
    }

    private fun doMySearch(query: String) {
    }

    override fun onSupportNavigateUp(): Boolean = findNavController(nav_host).navigateUp()

    fun setActionBarTitle(title: String) {
        toolbar.title = title
    }
}
