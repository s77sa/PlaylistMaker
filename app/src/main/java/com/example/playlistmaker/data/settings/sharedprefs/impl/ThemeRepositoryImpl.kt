package com.example.playlistmaker.data.settings.sharedprefs.impl

import com.example.playlistmaker.data.settings.sharedprefs.ThemeRepository
import com.example.playlistmaker.data.sharing.SharedPreferencesRepository

const val DARK_THEME = "dark_theme"

class ThemeRepositoryImpl(private val sharedPrefsRepository: SharedPreferencesRepository) : ThemeRepository {

    override fun saveTheme(status: Boolean) {
        val sharedPreferences = sharedPrefsRepository.getSharedPreferences()
        sharedPreferences.edit()
            .putBoolean(DARK_THEME, status)
            .apply()
    }

    override fun restoreTheme(): Boolean {
        val sharedPreferences = sharedPrefsRepository.getSharedPreferences()
        return sharedPreferences.getBoolean(DARK_THEME, false)
    }
}