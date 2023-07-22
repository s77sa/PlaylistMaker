package com.example.playlistmaker.data.search.sharedprefs

import com.example.playlistmaker.data.search.models.Track
import com.example.playlistmaker.data.search.models.Tracks

interface HistoryRepository {

    fun saveUserHistory(tracks: List<Track>)

    fun restoreUserHistory(): List<Track>?

    fun saveUserHistory2(tracks: Tracks)

    fun restoreUserHistory2(): Tracks?
}