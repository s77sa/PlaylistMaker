package com.example.playlistmaker.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.playlistmaker.data.db.dao.FavoritesTrackDao
import com.example.playlistmaker.data.db.entity.FavoritesTrackEntity

@Database(version = 1, entities = [FavoritesTrackEntity::class])
abstract class AppDatabase : RoomDatabase() {

    abstract fun favoritesTrackDao() : FavoritesTrackDao
}