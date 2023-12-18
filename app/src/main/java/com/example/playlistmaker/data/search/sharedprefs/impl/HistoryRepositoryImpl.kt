package com.example.playlistmaker.data.search.sharedprefs.impl

import android.annotation.SuppressLint
import android.content.SharedPreferences
import com.example.playlistmaker.data.models.Track
import com.example.playlistmaker.data.models.Tracks
import com.example.playlistmaker.domain.search.sharedprefs.HistoryRepository
import com.google.gson.Gson

const val TRACKS_HISTORY = "tracks_history"

class HistoryRepositoryImpl(
    private val sharedPreferences: SharedPreferences,
    private val gson: Gson
) : HistoryRepository {

    @SuppressLint("SuspiciousIndentation")
    override fun saveUserHistory(tracks: List<Track>) {
        val json = gson.toJson(tracks)
        sharedPreferences.edit()
            .putString(TRACKS_HISTORY, json)
            .apply()
    }

    override fun restoreUserHistory(): List<Track>? {
        var tracks: List<Track>? = null
        val json = sharedPreferences.getString(TRACKS_HISTORY, null)
        if (json != null) {
            tracks = gson.fromJson<List<Track>>(json, List::class.java)
        }
        return tracks
    }

    override fun saveUserHistoryTracks(tracks: Tracks) {
        val json = gson.toJson(tracks)
        sharedPreferences.edit()
            .putString(TRACKS_HISTORY, json)
            .apply()
    }

    override fun restoreUserHistoryTracks(): Tracks? {
        var tracks: Tracks? = null
        val json = sharedPreferences.getString(TRACKS_HISTORY, null)
        if (json != null) {
            tracks = gson.fromJson(json, Tracks::class.java)
        }
        return tracks
    }
}