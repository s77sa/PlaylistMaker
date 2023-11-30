package com.example.playlistmaker.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.playlistmaker.data.db.entity.FavoritesTrackEntity

@Dao
interface FavoritesTrackDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavoritesTracks(tracks: List<FavoritesTrackEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavoritesTrack(track: FavoritesTrackEntity)

    @Query("SELECT * FROM favorites_table ORDER BY sequenceId DESC")
    suspend fun getFavoritesTrack(): List<FavoritesTrackEntity>

    @Query("SELECT COUNT() FROM favorites_table WHERE trackId = :trackId")
    suspend fun checkFavoritesTrack(trackId: Int): Int

    @Query("DELETE FROM favorites_table WHERE trackId = :trackId")
    suspend fun deleteFavoritesTrack(trackId: Int)

}