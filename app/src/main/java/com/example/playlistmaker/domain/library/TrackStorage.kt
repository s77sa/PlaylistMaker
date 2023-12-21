package com.example.playlistmaker.domain.library

import com.example.playlistmaker.data.models.Track

interface TrackStorage {
    fun setTrack(track: Track)
    fun getTrack(): Track?
}