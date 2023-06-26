package com.example.playlistmaker.domain

import android.app.Application
import android.util.Log
import androidx.appcompat.app.AppCompatDelegate
import com.example.playlistmaker.data.sp.Preferences

const val HISTORY_COUNT: Int = 10
const val PLAY_LIST_PREFERENCES = "play_list_preferences"

class App : Application() {
    private lateinit var preferences: Preferences
    var darkTheme = false
    override fun onCreate() {
        super.onCreate()

        Log.println(Log.INFO, "my_tag", "onCreate.App")
        preferences = Preferences(getSharedPreferences(PLAY_LIST_PREFERENCES, MODE_PRIVATE))
        initApplicationPreferences()
    }

    private fun initApplicationPreferences() {
        darkTheme = preferences.restoreTheme()
        switchTheme()
    }

    fun switchThemeAndSave(darkThemeEnabled: Boolean) {
        darkTheme = darkThemeEnabled
        switchTheme()
        preferences.saveTheme(darkThemeEnabled)
    }

    private fun switchTheme() {
        AppCompatDelegate.setDefaultNightMode(
            if (darkTheme) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
    }
}