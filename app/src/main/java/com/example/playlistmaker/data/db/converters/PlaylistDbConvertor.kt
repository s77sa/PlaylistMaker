package com.example.playlistmaker.data.db.converters

import com.example.playlistmaker.data.db.entity.PlaylistsEntity
import com.example.playlistmaker.data.models.Playlist

class PlaylistDbConvertor {

    fun map(playlist: Playlist): PlaylistsEntity {
        return PlaylistsEntity(
            id = playlist.id,
            name = playlist.name,
            description = playlist.description,
            imagePath = playlist.imagePath
        )
    }

    fun map(playlist: PlaylistsEntity): Playlist {
        return Playlist(
            id = playlist.id,
            name = playlist.name,
            description = playlist.description!!,
            imagePath = playlist.imagePath!!,
            tracksCount = 0
        )
    }
}