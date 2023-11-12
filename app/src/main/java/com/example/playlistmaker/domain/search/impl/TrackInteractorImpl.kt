package com.example.playlistmaker.domain.search.impl

import com.example.playlistmaker.data.search.models.Track
import com.example.playlistmaker.data.search.network.retrofit.models.ConnectionStatus
import com.example.playlistmaker.data.search.network.retrofit.models.Resource
import com.example.playlistmaker.data.search.network.retrofit.TrackRepository
import com.example.playlistmaker.domain.search.TrackInteractor
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.concurrent.Executors

class TrackInteractorImpl(private val repository: TrackRepository) : TrackInteractor {

    //    private val executor = Executors.newCachedThreadPool()
//
//    override fun searchTracks(expression: String, consumer: TrackInteractor.TracksConsumer) {
//        executor.execute {
//            when(val resource = repository.searchTracks(expression)){
//                is Resource.Success -> { consumer.consume(resource.data, ConnectionStatus.SUCCESS)}
//                is Resource.Error -> { consumer.consume(null, ConnectionStatus.CONNECTION_ERROR)}
//            }
//        }
//    }
    override fun searchTracks(expression: String): Flow<Pair<List<Track>?, ConnectionStatus>> {
        return repository.searchTracks(expression).map { result ->
            when (result) {
                is Resource.Success -> {
                    Pair(result.data, ConnectionStatus.SUCCESS)
                }

                is Resource.Error -> {
                    Pair(null, ConnectionStatus.CONNECTION_ERROR)
                }
            }
        }
    }
}
