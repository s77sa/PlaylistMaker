package com.example.playlistmaker.data.settings.sharedprefs

import android.content.Context

interface ThemeRepository {
    fun saveTheme(status: Boolean)

    fun restoreTheme(): Boolean
}