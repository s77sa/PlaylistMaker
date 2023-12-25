package com.example.playlistmaker.data.db.converters

import com.example.playlistmaker.data.db.entity.FavoritesTrackEntity
import com.example.playlistmaker.domain.model.Track

class FavoritesTrackDbConvertor {

    fun map(track: Track): FavoritesTrackEntity {
        return FavoritesTrackEntity(
            sequenceId = 0L,
            trackId = track.trackId!!,
            artistName = track.artistName!!,
            artworkUrl100 = track.artworkUrl100!!,
            collectionName = track.collectionName!!,
            country = track.country!!,
            previewUrl = track.previewUrl!!,
            trackName = track.trackName!!,
            trackTimeMillis = track.trackTimeMillis!!,
            primaryGenreName = track.primaryGenreName!!,
            releaseDate = track.releaseDate!!
        )
    }

    fun map(track: FavoritesTrackEntity): Track {
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