package com.example.playlistmaker.data.settings.sharedprefs

interface ThemeRepository {
    fun saveTheme(status: Boolean)

    fun restoreTheme(): Boolean
}