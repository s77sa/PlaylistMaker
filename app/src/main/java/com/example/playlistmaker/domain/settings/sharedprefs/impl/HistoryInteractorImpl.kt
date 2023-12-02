package com.example.playlistmaker.domain.settings.sharedprefs.impl

import com.example.playlistmaker.data.search.models.Track
import com.example.playlistmaker.data.search.models.Tracks
import com.example.playlistmaker.domain.search.sharedprefs.HistoryRepository
import com.example.playlistmaker.domain.settings.sharedprefs.HistoryInteractor

class HistoryInteractorImpl(private val repository: HistoryRepository) : HistoryInteractor{
    override fun saveUserHistory(tracks: List<Track>) {
        return repository.saveUserHistory(tracks)
    }

    override fun restoreUserHistory(): List<Track>? {
        return repository.restoreUserHistory()
    }

    override fun saveUserHistoryTracks(tracks: Tracks){
        return repository.saveUserHistoryTracks(tracks)
    }

    override fun restoreUserHistoryTracks(): Tracks?{
        return repository.restoreUserHistoryTracks()
    }

}