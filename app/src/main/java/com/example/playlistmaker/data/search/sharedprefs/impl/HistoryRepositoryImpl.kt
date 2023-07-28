package com.example.playlistmaker.data.search.sharedprefs.impl

import com.example.playlistmaker.data.search.gson.GsonRepository
import com.example.playlistmaker.data.search.models.Track
import com.example.playlistmaker.data.search.models.Tracks
import com.example.playlistmaker.data.search.sharedprefs.HistoryRepository
import com.example.playlistmaker.data.sharing.SharedPreferencesRepository


const val TRACKS_HISTORY = "tracks_history"

class HistoryRepositoryImpl(private val sharedPrefsRepository: SharedPreferencesRepository, private val gsonRepository: GsonRepository) : HistoryRepository {

 override fun saveUserHistory(tracks: List<Track>) {
        val sharedPreferences = sharedPrefsRepository.getSharedPreferences()
     val gson = gsonRepository.getGson()
     val json = gson.toJson(tracks)
        sharedPreferences.edit()
            .putString(TRACKS_HISTORY, json)
            .apply()
    }

    override fun restoreUserHistory(): List<Track>? {
        val sharedPreferences = sharedPrefsRepository.getSharedPreferences()
        var tracks: List<Track>? = null
        val gson = gsonRepository.getGson()
        val json = sharedPreferences.getString(TRACKS_HISTORY, null)
        if (json != null) {
            tracks = gson.fromJson<List<Track>>(json, List::class.java)
        }
        return tracks
    }

    override fun saveUserHistoryTracks(tracks: Tracks) {
        val sharedPreferences = sharedPrefsRepository.getSharedPreferences()
        val gson = gsonRepository.getGson()
        val json = gson.toJson(tracks)
        sharedPreferences.edit()
            .putString(TRACKS_HISTORY, json)
            .apply()
    }

    override fun restoreUserHistoryTracks(): Tracks? {
        val sharedPreferences = sharedPrefsRepository.getSharedPreferences()
        val gson = gsonRepository.getGson()
        var tracks: Tracks? = null
        val json = sharedPreferences.getString(TRACKS_HISTORY, null)
        if (json != null) {
            tracks = gson.fromJson(json, Tracks::class.java)
        }
        return tracks
    }
}