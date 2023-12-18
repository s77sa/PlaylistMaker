package com.example.playlistmaker.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.playlistmaker.data.db.dao.FavoritesTrackDao
import com.example.playlistmaker.data.db.dao.PlaylistDao
import com.example.playlistmaker.data.db.dao.TracksInPlaylistsDao
import com.example.playlistmaker.data.db.entity.FavoritesTrackEntity
import com.example.playlistmaker.data.db.entity.PlaylistsEntity
import com.example.playlistmaker.data.db.entity.TracksInPlaylists

@Database(
    version = 1,
    entities = [
        FavoritesTrackEntity::class,
        PlaylistsEntity::class,
        TracksInPlaylists::class
    ]
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun favoritesTrackDao(): FavoritesTrackDao

    abstract fun playlistsDao(): PlaylistDao

    abstract fun tracksInPlaylistDao(): TracksInPlaylistsDao
}