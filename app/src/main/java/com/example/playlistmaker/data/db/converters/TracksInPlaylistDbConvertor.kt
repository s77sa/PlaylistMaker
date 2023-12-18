package com.example.playlistmaker.data.db.converters

import com.example.playlistmaker.data.db.entity.TracksInPlaylists
import com.example.playlistmaker.data.models.Track

class TracksInPlaylistDbConvertor {

    fun map(track: Track, playlistId: Int): TracksInPlaylists {
        return TracksInPlaylists(
            id = 0,
            trackId = track.trackId!!,
            artistName = track.artistName!!,
            artworkUrl100 = track.artworkUrl100!!,
            collectionName = track.collectionName!!,
            country = track.country!!,
            previewUrl = track.previewUrl!!,
            trackName = track.trackName!!,
            trackTimeMillis = track.trackTimeMillis!!,
            primaryGenreName = track.primaryGenreName!!,
            releaseDate = track.releaseDate!!,
            playlistId = playlistId
        )
    }

    fun map(track: TracksInPlaylists): Track {
        return Track(
            trackId = track.trackId,
            artistName = track.artistName,
            artworkUrl100 = track.artworkUrl100,
            collectionName = track.collectionName,
            country = track.country,
            previewUrl = track.previewUrl,
            trackName = track.trackName,
            trackTimeMillis = track.trackTimeMillis,
            primaryGenreName = track.primaryGenreName,
            releaseDate = track.releaseDate
        )
    }
}