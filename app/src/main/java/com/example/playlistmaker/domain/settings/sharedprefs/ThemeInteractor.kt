package com.example.playlistmaker.domain.settings.sharedprefs

interface ThemeInteractor {
    fun saveTheme(status: Boolean)

    fun restoreTheme(): Boolean
}