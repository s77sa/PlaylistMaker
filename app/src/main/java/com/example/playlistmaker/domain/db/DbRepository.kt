package com.example.playlistmaker.domain.db

import com.example.playlistmaker.data.models.Playlist
import com.example.playlistmaker.data.models.Track
import kotlinx.coroutines.flow.Flow

interface DbRepository {

    fun favoritesTracks(): Flow<List<Track>>

    fun playlists(): Flow<List<Playlist>>

    fun tracksInPlaylists(playlistId: Int): Flow<List<Track>>

    suspend fun countTracksInPlaylists(playlistId: Int): Int

}