package com.example.playlistmaker.domain.library

import com.example.playlistmaker.domain.model.Playlist

interface PlaylistStorage {
    fun setPlaylist(playlist: Playlist)

    fun getPlaylist(): Playlist?
}