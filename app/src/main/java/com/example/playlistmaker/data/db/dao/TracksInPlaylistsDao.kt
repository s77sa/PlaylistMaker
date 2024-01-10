package com.example.playlistmaker.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.playlistmaker.data.db.entity.TracksInPlaylistsEntity

@Dao
interface TracksInPlaylistsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTrackInPlaylist(track: TracksInPlaylistsEntity)

    @Query("DELETE FROM tracks_in_playlists_table WHERE id = :id")
    suspend fun deleteTrackInPlaylist(id: Int)

    @Query("SELECT * FROM tracks_in_playlists_table WHERE playlistId = :playlistId ORDER BY id DESC")
    suspend fun getTracksInPlaylist(playlistId: Int): List<TracksInPlaylistsEntity>

    @Query("DELETE FROM tracks_in_playlists_table WHERE playlistId = :playlistId")
    suspend fun deleteAllTrackInPlaylist(playlistId: Int)

    @Query("DELETE FROM tracks_in_playlists_table WHERE playlistId = :playlistId AND trackId = :trackId")
    suspend fun deleteTrackFromPlaylist(playlistId: Int, trackId: Int)

    @Query("SELECT COUNT() FROM tracks_in_playlists_table WHERE playlistId = :playlistId")
    suspend fun getCountTracksInPlaylist(playlistId: Int): Int

    //@Query("SELECT COUNT() FROM (SELECT trackId from tracks_in_playlists_table WHERE playlistId = :playlistId) WHERE trackId = :trackId")
    @Query("SELECT COUNT() FROM tracks_in_playlists_table WHERE playlistId = :playlistId AND trackId = :trackId")
    suspend fun checkTrackInPlaylist(playlistId: Int, trackId: Int): Int
}