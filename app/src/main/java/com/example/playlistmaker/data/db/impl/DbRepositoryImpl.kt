package com.example.playlistmaker.data.db.impl

import com.example.playlistmaker.data.db.AppDatabase
import com.example.playlistmaker.data.db.converters.FavoritesTrackDbConvertor
import com.example.playlistmaker.data.db.converters.PlaylistDbConvertor
import com.example.playlistmaker.data.db.converters.TracksInPlaylistDbConvertor
import com.example.playlistmaker.data.db.entity.FavoritesTrackEntity
import com.example.playlistmaker.data.db.entity.PlaylistsEntity
import com.example.playlistmaker.data.db.entity.TracksInPlaylists
import com.example.playlistmaker.data.models.Playlist
import com.example.playlistmaker.data.models.Track
import com.example.playlistmaker.domain.db.DbRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class DbRepositoryImpl(
    private val appDatabase: AppDatabase,
    private val trackDbConvertor: FavoritesTrackDbConvertor,
    private val playlistDbConvertor: PlaylistDbConvertor,
    private val tracksInPlaylistDbConvertor: TracksInPlaylistDbConvertor
) : DbRepository {
    override fun favoritesTracks(): Flow<List<Track>> = flow {
        val tracks = appDatabase.favoritesTrackDao().getFavoritesTrack()
        emit(convertFromTrackEntity(tracks))
    }

    private fun convertFromTrackEntity(tracks: List<FavoritesTrackEntity>): List<Track> {
        return tracks.map(trackDbConvertor::map)
    }

    override fun playlists(): Flow<List<Playlist>> = flow {
        val playlists = appDatabase.playlistsDao().getPlaylist()
        emit(convertFromPlaylistsEntity(playlists))
    }

    private fun convertFromPlaylistsEntity(playlists: List<PlaylistsEntity>): List<Playlist> {
        return playlists.map(playlistDbConvertor::map)
    }

    override fun tracksInPlaylists(playlistId: Int): Flow<List<Track>> = flow {
        val tracks = appDatabase.tracksInPlaylistDao().getTracksInPlaylist(playlistId)
        emit(convertFromTracksInPlaylistEntity(tracks))
    }

    private fun convertFromTracksInPlaylistEntity(tracks: List<TracksInPlaylists>): List<Track> {
        return tracks.map(tracksInPlaylistDbConvertor::map)
    }

    override suspend fun countTracksInPlaylists(playlistId: Int): Int {
       return appDatabase.tracksInPlaylistDao().getCountTracksInPlaylist(playlistId)
    }

}