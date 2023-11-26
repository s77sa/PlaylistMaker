package com.example.playlistmaker.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.playlistmaker.data.db.entity.HistoryTrackEntity
import com.example.playlistmaker.data.search.models.Tracks

@Dao
interface HistoryTrackDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHistoryTrack(tracks: List<HistoryTrackEntity>)

    @Query("SELECT * FROM history_table")
    suspend fun getHistoryTrack(): List<HistoryTrackEntity>
}