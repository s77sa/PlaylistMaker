package com.example.playlistmaker.domain.db.impl

import com.example.playlistmaker.domain.model.Playlist
import com.example.playlistmaker.data.models.Track
import com.example.playlistmaker.domain.db.DbInteractor
import com.example.playlistmaker.domain.db.DbRepository
import kotlinx.coroutines.flow.Flow

class DbInteractorImpl(
    private val dbRepository: DbRepository
) : DbInteractor {
    override fun favoritesTracks(): Flow<List<Track>> {
        return  dbRepository.favoritesTracks()
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
}