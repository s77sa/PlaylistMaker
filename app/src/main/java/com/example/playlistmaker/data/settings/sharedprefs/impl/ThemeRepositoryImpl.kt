package com.example.playlistmaker.data.settings.sharedprefs.impl

import android.content.Context
import com.example.playlistmaker.data.settings.sharedprefs.ThemeRepository

const val DARK_THEME = "dark_theme"
const val PLAY_LIST_PREFERENCES = "play_list_preferences"

class ThemeRepositoryImpl(context: Context) : ThemeRepository {

        private val sharedPreferences = context.getSharedPreferences(
            PLAY_LIST_PREFERENCES,
            Context.MODE_PRIVATE
        )

    override fun saveTheme(status: Boolean) {
        sharedPreferences.edit()
            .putBoolean(DARK_THEME, status)
            .apply()
    }

    override fun restoreTheme(): Boolean {
        return sharedPreferences.getBoolean(DARK_THEME, false)
    }
}