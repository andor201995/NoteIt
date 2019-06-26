package com.andor.navigate.notepad.listing

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.NavHostFragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import com.andor.navigate.notepad.R
import com.andor.navigate.notepad.core.NoteViewModel
import com.andor.navigate.notepad.core.NoteViewModelFactory
import com.andor.navigate.notepad.listing.dao.NoteRepoImpl
import kotlinx.android.synthetic.main.activity_listing.*
import kotlinx.android.synthetic.main.content_main.*
import org.koin.android.ext.android.inject
import org.koin.core.parameter.parametersOf

class NotesActivity : AppCompatActivity() {
    private lateinit var viewModel: NoteViewModel
    private val repoImpl by inject<NoteRepoImpl> { parametersOf(intent!!.getStringExtra("uid")!!) }

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

        viewModel =
            ViewModelProviders.of(this, NoteViewModelFactory(repoImpl))
                .get(NoteViewModel::class.java)
    }

    override fun onSupportNavigateUp(): Boolean = findNavController(nav_host).navigateUp()

    fun setActionBarTitle(title: String) {
        toolbar.title = title
    }
}
