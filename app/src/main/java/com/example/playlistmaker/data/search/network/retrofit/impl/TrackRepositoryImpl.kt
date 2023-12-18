package com.example.playlistmaker.data.search.network.retrofit.impl

import com.example.playlistmaker.data.models.Track
import com.example.playlistmaker.data.search.network.retrofit.TrackRepository
import com.example.playlistmaker.data.search.network.retrofit.models.ConnectionStatus
import com.example.playlistmaker.data.search.network.retrofit.models.NetworkClient
import com.example.playlistmaker.data.search.network.retrofit.models.Resource
import com.example.playlistmaker.data.search.network.retrofit.models.TracksSearchRequest
import com.example.playlistmaker.data.search.network.retrofit.models.TracksSearchResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow


class TrackRepositoryImpl(private val networkClient: NetworkClient) : TrackRepository {

    override fun searchTracks(expression: String): Flow<Resource<List<Track>>> = flow {
        val response = networkClient.doRequest(TracksSearchRequest(expression))
        when (response.resultCode) {
            -1 -> {
                emit(Resource.Error(ConnectionStatus.CONNECTION_ERROR))
            }

            200 -> {
                with(response as TracksSearchResponse) {
                    val data = results.map {
                        Track(
                            trackName = it.trackName,
                            artistName = it.artistName,
                            trackTimeMillis = it.trackTimeMillis,
                            artworkUrl100 = it.artworkUrl100,
                            trackId = it.trackId,
                            collectionName = it.collectionName,
                            releaseDate = it.releaseDate,
                            primaryGenreName = it.primaryGenreName,
                            country = it.country,
                            previewUrl = it.previewUrl
                        )
                    }
                    emit(Resource.Success(data))
                }
            }

            else -> {
                emit(Resource.Error(ConnectionStatus.SERVER_ERROR))
            }
        }
    }
}
