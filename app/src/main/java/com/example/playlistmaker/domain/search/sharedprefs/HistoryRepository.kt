package com.example.playlistmaker.domain.search.sharedprefs

import com.example.playlistmaker.data.search.models.Track
import com.example.playlistmaker.data.search.models.Tracks

interface HistoryRepository {

    fun saveUserHistory(tracks: List<Track>)

    fun restoreUserHistory(): List<Track>?

    fun saveUserHistoryTracks(tracks: Tracks)

    fun restoreUserHistoryTracks(): Tracks?
}