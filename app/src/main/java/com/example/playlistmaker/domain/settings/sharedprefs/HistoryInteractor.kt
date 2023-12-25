package com.example.playlistmaker.domain.settings.sharedprefs

import com.example.playlistmaker.domain.model.Track
import com.example.playlistmaker.data.models.Tracks

interface HistoryInteractor {
    fun saveUserHistory(tracks: List<Track>)

    fun restoreUserHistory(): List<Track>?

    fun saveUserHistoryTracks(tracks: Tracks)

    fun restoreUserHistoryTracks(): Tracks?
}