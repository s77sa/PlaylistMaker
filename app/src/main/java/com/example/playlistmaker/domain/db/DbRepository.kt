package com.example.playlistmaker.domain.db

import com.example.playlistmaker.domain.model.Playlist
import com.example.playlistmaker.domain.model.Track
import kotlinx.coroutines.flow.Flow

interface DbRepository {

    fun favoritesTracks(): Flow<List<Track>>

    fun playlists(): Flow<List<Playlist>>

    fun tracksInPlaylists(playlistId: Int): Flow<List<Track>>

    suspend fun countTracksInPlaylists(playlistId: Int): Int

    suspend fun checkTrackInPlaylist(playlistId: Int, trackId: Int): Int

    suspend fun addPlaylist(playlist: Playlist)

    suspend fun insertTrackInPlaylist(track: Track, playlistId: Int)

    suspend fun insertFavoritesTrack(track: Track)

    suspend fun deleteFavoritesTrack(track: Track)

    suspend fun checkFavoritesTrack(track: Track): Boolean

    suspend fun deleteTrackFromPlaylist(playlistId: Int, trackId: Int)

    suspend fun deleteAllTrackInPlaylist(playlistId: Int)

    suspend fun deletePlaylist(id: Int)

    suspend fun updatePlaylist(playlist: Playlist)

}