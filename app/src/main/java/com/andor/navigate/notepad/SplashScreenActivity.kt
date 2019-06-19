package com.andor.navigate.notepad

import android.graphics.Typeface
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.andor.navigate.notepad.auth.AuthActivity
import kotlinx.android.synthetic.main.activity_splash_screen.*


class SplashScreenActivity : AppCompatActivity() {
    // Splash screen timer
    private val splashTime = 500L


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        val font = Typeface.createFromAsset(assets, "font/note_splash_font.ttf")
        splash_text.typeface = font

        Handler().postDelayed(
            {
                startNoteActivity()
            }, splashTime
        )
    }

    private fun startNoteActivity() {
        val i = AuthActivity.intent(this@SplashScreenActivity)
        startActivity(i)
        finish()
    }
}
