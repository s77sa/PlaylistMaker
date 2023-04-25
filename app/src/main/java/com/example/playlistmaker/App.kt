package com.example.playlistmaker

import android.app.Application
import android.content.SharedPreferences
import android.util.Log
import androidx.appcompat.app.AppCompatDelegate

const val PLAY_LIST_PREFERENCES = "play_list_preferences"
const val DARK_THEME = "dark_theme"

class App : Application() {
    private lateinit var sharedPrefs: SharedPreferences
    var darkTheme = false
    override fun onCreate() {
        super.onCreate()
        Log.println(Log.INFO, "my_tag", "onCreate.App")
        sharedPrefs = getSharedPreferences(PLAY_LIST_PREFERENCES, MODE_PRIVATE)
        initApplicationPreferences()
    }

    private fun initApplicationPreferences(){
        restorePrefs()
        switchTheme()
    }

    fun switchThemeAndSave(darkThemeEnabled: Boolean) {
        darkTheme = darkThemeEnabled
        switchTheme()
        savePrefs()
    }

    fun switchTheme() {
        AppCompatDelegate.setDefaultNightMode(
            if (darkTheme) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO }
        )
    }

    private fun savePrefs(){
        sharedPrefs.edit()
            .putBoolean(DARK_THEME, darkTheme)
            .apply()
    }
    private fun restorePrefs(){
        darkTheme =  sharedPrefs.getBoolean(DARK_THEME, false)
    }
}