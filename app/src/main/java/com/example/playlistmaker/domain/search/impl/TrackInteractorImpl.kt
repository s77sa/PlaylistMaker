package com.example.playlistmaker.domain.search.impl

import com.example.playlistmaker.data.search.network.retrofit.ConnectionStatus
import com.example.playlistmaker.data.search.network.retrofit.Resource
import com.example.playlistmaker.data.search.network.retrofit.TrackRepository
import com.example.playlistmaker.domain.search.TrackInteractor
import java.util.concurrent.Executors

class TrackInteractorImpl(private val repository: TrackRepository): TrackInteractor {

    private val executor = Executors.newCachedThreadPool()

    override fun searchTracks(expression: String, consumer: TrackInteractor.TracksConsumer) {
        executor.execute {
            when(val resource = repository.searchTracks(expression)){
                is Resource.Success -> { consumer.consume(resource.data, ConnectionStatus.SUCCESS)}
                is Resource.Error -> { consumer.consume(null, ConnectionStatus.CONNECTION_ERROR)}
            }
        }
    }
}
