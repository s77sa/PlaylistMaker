package com.example.playlistmaker.data.search.network.retrofit

import com.example.playlistmaker.data.search.models.Track

interface TrackRepository {
    fun searchTracks(expression: String): Resource<List<Track>>
}