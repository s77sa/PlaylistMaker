package com.example.playlistmaker.domain.settings.sharedprefs

interface ThemeRepository {
    fun saveTheme(status: Boolean)

    fun restoreTheme(): Boolean
}