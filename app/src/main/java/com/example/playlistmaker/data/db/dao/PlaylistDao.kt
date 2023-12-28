package com.example.playlistmaker.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.playlistmaker.data.db.entity.PlaylistsEntity

@Dao
interface PlaylistDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addPlaylist(playlist: PlaylistsEntity)

    @Query("DELETE FROM playlists_table WHERE id = :id")
    suspend fun deletePlaylist(id: Int)

    @Query("SELECT * FROM playlists_table ORDER BY id DESC")
    suspend fun getPlaylist(): List<PlaylistsEntity>

}