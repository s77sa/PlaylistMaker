package com.example.playlistmaker.data.settings.sharedprefs.impl

import android.content.SharedPreferences
import com.example.playlistmaker.data.settings.sharedprefs.ThemeRepository

const val DARK_THEME = "dark_theme"

class ThemeRepositoryImpl(
    private val sharedPreferences: SharedPreferences
) : ThemeRepository {
    override fun saveTheme(status: Boolean) {
        sharedPreferences.edit()
            .putBoolean(DARK_THEME, status)
            .apply()
    }

    override fun restoreTheme(): Boolean {
        return sharedPreferences.getBoolean(DARK_THEME, false)
    }
}