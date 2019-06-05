package com.andor.navigate.notepad.core

import android.os.SystemClock
import android.view.View
import java.util.*

abstract class DebouncedOnClickListener
    (private val minimumInterval: Long) : View.OnClickListener {
    private val lastClickMap: MutableMap<View, Long>
    abstract fun onDebouncedClick(v: View)

    init {
        this.lastClickMap = WeakHashMap()
    }

    override fun onClick(clickedView: View) {
        val previousClickTimestamp = lastClickMap[clickedView]
        val currentTimestamp = SystemClock.uptimeMillis()

        lastClickMap[clickedView] = currentTimestamp
        if (previousClickTimestamp == null || Math.abs(currentTimestamp - previousClickTimestamp.toLong()) > minimumInterval) {
            onDebouncedClick(clickedView)
        }
    }
}