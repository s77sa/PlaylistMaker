package com.example.playlistmaker.data.db.converters

import com.example.playlistmaker.data.db.entity.HistoryTrackEntity
import com.example.playlistmaker.data.search.models.Track

class HistoryTrackDbConvertor {

    fun map(track: Track): HistoryTrackEntity {
        return HistoryTrackEntity(
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

    fun map(track: HistoryTrackEntity): Track {
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