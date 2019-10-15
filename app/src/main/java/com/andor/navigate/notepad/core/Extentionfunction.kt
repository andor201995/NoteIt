package com.andor.navigate.notepad.core

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
