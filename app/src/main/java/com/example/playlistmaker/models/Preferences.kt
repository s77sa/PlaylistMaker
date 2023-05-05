package com.example.playlistmaker.models

import android.content.SharedPreferences
import com.example.playlistmaker.retrofit.Tracks
import com.google.gson.Gson

const val DARK_THEME = "dark_theme"
const val TRACKS_HISTORY = "tracks_history"

class Preferences(
    private val sharedPreferences: SharedPreferences
) {

    fun saveTheme(status: Boolean) {
        sharedPreferences.edit()
            .putBoolean(DARK_THEME, status)
            .apply()
    }

    fun restoreTheme(): Boolean {
        return sharedPreferences.getBoolean(DARK_THEME, false)
    }

    fun saveUserHistory(tracks: Tracks) {
        val json = Gson().toJson(tracks)
        sharedPreferences.edit()
            .putString(TRACKS_HISTORY, json)
            .apply()
    }

    fun restoreUserHistory(): Tracks? {
        var tracks: Tracks? = null
        val json = sharedPreferences.getString(TRACKS_HISTORY, null)
        if (json != null) {
            tracks = Gson().fromJson(json, Tracks::class.java)
        }
        return tracks
    }
}