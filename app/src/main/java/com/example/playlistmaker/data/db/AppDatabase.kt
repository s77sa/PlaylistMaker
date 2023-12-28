package com.example.playlistmaker.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.playlistmaker.data.db.dao.FavoritesTrackDao
import com.example.playlistmaker.data.db.dao.PlaylistDao
import com.example.playlistmaker.data.db.dao.TracksInPlaylistsDao
import com.example.playlistmaker.data.db.entity.FavoritesTrackEntity
import com.example.playlistmaker.data.db.entity.PlaylistsEntity
import com.example.playlistmaker.data.db.entity.TracksInPlaylistsEntity

@Database(
    version = 2,
    entities = [
        FavoritesTrackEntity::class,
        PlaylistsEntity::class,
        TracksInPlaylistsEntity::class
    ],
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun favoritesTrackDao(): FavoritesTrackDao

    abstract fun playlistsDao(): PlaylistDao

    abstract fun tracksInPlaylistDao(): TracksInPlaylistsDao

}