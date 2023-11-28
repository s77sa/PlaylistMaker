package com.example.playlistmaker.domain.db

import com.example.playlistmaker.data.db.AppDatabase
import com.example.playlistmaker.data.db.converters.FavoritesTrackDbConvertor
import com.example.playlistmaker.data.db.entity.FavoritesTrackEntity
import com.example.playlistmaker.data.search.models.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FavoritesRepositoryImpl (
    private val appDatabase: AppDatabase,
    private val trackDbConvertor: FavoritesTrackDbConvertor
) : FavoritesRepository{
    override fun favoritesTracks(): Flow<List<Track>> = flow {
        val tracks = appDatabase.favoritesTrackDao().getFavoritesTrack()
        emit(convertFromTrackEntity(tracks))
    }

    private fun convertFromTrackEntity(tracks: List<FavoritesTrackEntity>) : List<Track>{
        return tracks.map(trackDbConvertor::map)
    }

}