package com.theapache64.tracktor.data.repositories

import android.content.SharedPreferences
import androidx.core.content.edit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PrefRepo @Inject constructor(
    private val sharedPreferences: SharedPreferences
) {

    companion object {
        private const val KEY_IS_NIGHT_MODE = "is_night_mode_enabled"
    }

    fun isNightModeEnabled(): Boolean = sharedPreferences.getBoolean(
        KEY_IS_NIGHT_MODE,
        true
    )

    fun setNightModeEnabled(isEnabled: Boolean) =
        sharedPreferences.edit {
            putBoolean(KEY_IS_NIGHT_MODE, isEnabled)
        }
}