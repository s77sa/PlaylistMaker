package com.example.playlistmaker.data.search.network.retrofit

import com.example.playlistmaker.data.models.Track
import com.example.playlistmaker.data.search.network.retrofit.models.Resource
import kotlinx.coroutines.flow.Flow

interface TrackRepository {
    fun searchTracks(expression: String): Flow<Resource<List<Track>>>
}