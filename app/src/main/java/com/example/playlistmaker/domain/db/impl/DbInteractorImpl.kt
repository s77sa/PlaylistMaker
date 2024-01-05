package com.example.playlistmaker.domain.db.impl

import com.example.playlistmaker.domain.model.Track
import com.example.playlistmaker.domain.db.DbInteractor
import com.example.playlistmaker.domain.db.DbRepository
import com.example.playlistmaker.domain.model.Playlist
import kotlinx.coroutines.flow.Flow

class DbInteractorImpl(
    private val dbRepository: DbRepository
) : DbInteractor {
    override fun favoritesTracks(): Flow<List<Track>> {
        return dbRepository.favoritesTracks()
    }

    override fun playlists(): Flow<List<Playlist>> {
        return dbRepository.playlists()
    }

    override fun tracksInPlaylists(playlistId: Int): Flow<List<Track>> {
        return dbRepository.tracksInPlaylists(playlistId)
    }

    override suspend fun countTracksInPlaylists(playlistId: Int): Int {
        return dbRepository.countTracksInPlaylists(playlistId)
    }

    override suspend fun checkTrackInPlaylist(playlistId: Int, trackId: Int): Int {
        return dbRepository.checkTrackInPlaylist(playlistId, trackId)
    }

    override suspend fun addPlaylist(playlist: Playlist) {
        dbRepository.addPlaylist(playlist)
    }

    override suspend fun insertTrackInPlaylist(track: Track, playlistId: Int) {
        dbRepository.insertTrackInPlaylist(track, playlistId)
    }

    override suspend fun insertFavoritesTrack(track: Track) {
        dbRepository.insertFavoritesTrack(track)
    }

    override suspend fun deleteFavoritesTrack(track: Track) {
        dbRepository.deleteFavoritesTrack(track)
    }

    override suspend fun checkFavoritesTrack(track: Track): Boolean {
        return dbRepository.checkFavoritesTrack(track)
    }

    override suspend fun deleteTrackFromPlaylist(playlistId: Int, trackId: Int) {
        return dbRepository.deleteTrackFromPlaylist(playlistId, trackId)
    }

    override suspend fun deleteAllTrackInPlaylist(playlistId: Int) {
        return dbRepository.deleteAllTrackInPlaylist(playlistId)
    }

    override suspend fun deletePlaylist(id: Int) {
        return dbRepository.deletePlaylist(id)
    }
}