package com.example.playlistmaker.domain.search

import com.example.playlistmaker.data.search.models.Track
import com.example.playlistmaker.data.search.network.retrofit.ConnectionStatus

interface TrackInteractor {
    fun searchTracks(expression: String, consumer: TracksConsumer)

    interface TracksConsumer {
        fun consume(foundMovies: List<Track>?, errorMessage: ConnectionStatus)
    }
}
