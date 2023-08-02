package com.example.playlistmaker.data.search.network.retrofit.impl

import com.example.playlistmaker.data.search.models.Track
import com.example.playlistmaker.data.search.network.retrofit.TrackRepository
import com.example.playlistmaker.data.search.network.retrofit.models.ConnectionStatus
import com.example.playlistmaker.data.search.network.retrofit.models.NetworkClient
import com.example.playlistmaker.data.search.network.retrofit.models.Resource
import com.example.playlistmaker.data.search.network.retrofit.models.TracksSearchRequest
import com.example.playlistmaker.data.search.network.retrofit.models.TracksSearchResponse

class TrackRepositoryImpl(private val networkClient: NetworkClient) : TrackRepository {
    override fun searchTracks(expression: String): Resource<List<Track>> {

        val response = networkClient.doRequest(TracksSearchRequest(expression))
        return when (response.resultCode) {
            -1 -> {
                Resource.Error(ConnectionStatus.CONNECTION_ERROR)
            }

            200 -> {
                Resource.Success((response as TracksSearchResponse).results.map {
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
                })
            }

            else -> {
                Resource.Error(ConnectionStatus.SERVER_ERROR)
            }
        }
    }
}
