package com.example.playlistmaker.domain.settings.sharedprefs

import com.example.playlistmaker.data.search.models.Track
import com.example.playlistmaker.data.search.models.Tracks

interface HistoryInteractor {
    fun saveUserHistory(tracks: List<Track>)

    fun restoreUserHistory(): List<Track>?

    fun saveUserHistoryTracks(tracks: Tracks)

    fun restoreUserHistoryTracks(): Tracks?
}