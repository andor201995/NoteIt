package com.andor.navigate.notepad.core

import android.view.View
import androidx.navigation.NavController
import androidx.navigation.NavDirections

fun NavController.navigateSafe(navFragmentResId: Int, destResId: NavDirections) {
    if (currentDestination!!.id == navFragmentResId) {
        this.navigate(destResId)
    }
}

fun NavController.navigateSafe(
    navFragmentResId: Int,
    destResId: Int
) {
    if (currentDestination!!.id == navFragmentResId) {
        this.navigate(destResId)
    }
}


fun View.visible() {
    this.visibility = View.VISIBLE
}

fun View.inVisible() {
    this.visibility = View.GONE
}

fun View.isVisible(): Boolean {
    return this.visibility == View.VISIBLE
}
