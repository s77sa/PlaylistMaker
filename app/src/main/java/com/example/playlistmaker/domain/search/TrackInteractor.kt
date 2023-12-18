package com.example.playlistmaker.domain.search

import com.example.playlistmaker.data.models.Track
import com.example.playlistmaker.data.search.network.retrofit.models.ConnectionStatus
import kotlinx.coroutines.flow.Flow

interface TrackInteractor {
    fun searchTracks(expression: String): Flow<Pair<List<Track>?, ConnectionStatus>>
}
