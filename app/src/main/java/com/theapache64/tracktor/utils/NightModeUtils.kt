package com.theapache64.tracktor.utils

import androidx.appcompat.app.AppCompatDelegate

object NightModeUtils {

    fun setNightModeEnabled(isEnabled: Boolean) {
        val nightModeFlag = if (isEnabled) {
            AppCompatDelegate.MODE_NIGHT_YES
        } else {
            AppCompatDelegate.MODE_NIGHT_NO
        }

        AppCompatDelegate.setDefaultNightMode(nightModeFlag)
    }
}