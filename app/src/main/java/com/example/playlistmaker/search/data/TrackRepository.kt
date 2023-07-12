package com.example.playlistmaker.search.data

import com.example.playlistmaker.domain.models.track.Track

interface TrackRepository {
    fun searchTracks(expression: String): MutableList<Track>
}