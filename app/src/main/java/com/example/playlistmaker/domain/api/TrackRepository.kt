package com.example.playlistmaker.domain.api

import com.example.playlistmaker.domain.models.track.Track

interface TrackRepository {
    fun searchTracks(expression: String): MutableList<Track>
}