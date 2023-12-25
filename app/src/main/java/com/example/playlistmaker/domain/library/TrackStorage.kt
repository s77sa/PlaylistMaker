package com.example.playlistmaker.domain.library

import com.example.playlistmaker.domain.model.Track

interface TrackStorage {
    fun setTrack(track: Track)
    fun getTrack(): Track?
}