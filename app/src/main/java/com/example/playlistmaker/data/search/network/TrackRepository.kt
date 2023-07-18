package com.example.playlistmaker.data.search.network

import com.example.playlistmaker.data.search.models.Track

interface TrackRepository {
    fun searchTracks(expression: String): MutableList<Track>
}