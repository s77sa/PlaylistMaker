package com.example.playlistmaker.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.playlistmaker.data.db.dao.HistoryTrackDao
import com.example.playlistmaker.data.db.entity.HistoryTrackEntity

@Database(version = 1, entities = [HistoryTrackEntity::class])
abstract class AppDatabase : RoomDatabase() {

    abstract fun historyTrackDao() : HistoryTrackDao
}