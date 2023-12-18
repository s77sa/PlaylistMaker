package com.example.playlistmaker.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.playlistmaker.data.db.entity.TracksInPlaylists

@Dao
interface TracksInPlaylistsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTrackInPlaylist(track: List<TracksInPlaylists>)

    @Query("DELETE FROM tracks_in_playlists_table WHERE id = :id")
    suspend fun deleteTrackInPlaylist(id: Int)

    @Query("SELECT * FROM tracks_in_playlists_table WHERE playlistId = :playlistId")
    suspend fun getTracksInPlaylist(playlistId: Int): List<TracksInPlaylists>

    @Query("DELETE FROM tracks_in_playlists_table WHERE playlistId = :playlistId")
    suspend fun deleteAllTrackInPlaylist(playlistId: Int)

    @Query("SELECT COUNT() FROM tracks_in_playlists_table WHERE playlistId = :playlistId")
    suspend fun getCountTracksInPlaylist(playlistId: Int): Int
}