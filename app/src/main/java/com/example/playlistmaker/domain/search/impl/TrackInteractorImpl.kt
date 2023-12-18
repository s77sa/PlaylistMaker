package com.example.playlistmaker.domain.search.impl

import com.example.playlistmaker.data.models.Track
import com.example.playlistmaker.data.search.network.retrofit.TrackRepository
import com.example.playlistmaker.data.search.network.retrofit.models.ConnectionStatus
import com.example.playlistmaker.data.search.network.retrofit.models.Resource
import com.example.playlistmaker.domain.search.TrackInteractor
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class TrackInteractorImpl(private val repository: TrackRepository) : TrackInteractor {

    override fun searchTracks(expression: String): Flow<Pair<List<Track>?, ConnectionStatus>> {
        return repository.searchTracks(expression).map { trackResult ->
            when (trackResult) {
                is Resource.Success -> {
                    Pair(trackResult.data, trackResult.message)
                }

                is Resource.Error -> {
                    Pair(null, trackResult.message)
                }
            }
        }
    }
}
