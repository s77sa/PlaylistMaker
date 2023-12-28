package com.example.playlistmaker.data.settings.sharedprefs.impl

import android.content.SharedPreferences
import android.content.res.Resources
import com.example.playlistmaker.domain.settings.sharedprefs.ThemeRepository

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
        return sharedPreferences.getBoolean(DARK_THEME, checkSystemTheme())
    }

    private fun checkSystemTheme(): Boolean{
        return Resources.getSystem().configuration.uiMode == DARK_UI_YES

    }

    companion object {
        private const val DARK_UI_YES = 33
    }
}