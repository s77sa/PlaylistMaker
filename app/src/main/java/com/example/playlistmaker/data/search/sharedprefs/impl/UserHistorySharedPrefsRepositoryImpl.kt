package com.example.playlistmaker.data.search.sharedprefs.impl

import android.content.Context
import com.example.playlistmaker.data.search.models.Tracks
import com.example.playlistmaker.data.search.sharedprefs.UserHistorySharedPrefsRepository
import com.google.gson.Gson

const val TRACKS_HISTORY = "tracks_history"
const val PLAY_LIST_PREFERENCES = "play_list_preferences"

class UserHistorySharedPrefsRepositoryImpl(context: Context) : UserHistorySharedPrefsRepository {

        private val sharedPreferences = context.getSharedPreferences(
            PLAY_LIST_PREFERENCES,
            Context.MODE_PRIVATE
        )

    override fun saveUserHistory(tracks: Tracks) {
        val json = Gson().toJson(tracks)
        sharedPreferences.edit()
            .putString(TRACKS_HISTORY, json)
            .apply()
    }

    override fun restoreUserHistory(): Tracks? {
        var tracks: Tracks? = null
        val json = sharedPreferences.getString(TRACKS_HISTORY, null)
        if (json != null) {
            tracks = Gson().fromJson(json, Tracks::class.java)
        }
        return tracks
    }
}