package com.example.playlistmaker.domain.db

import com.example.playlistmaker.data.search.models.Track
import kotlinx.coroutines.flow.Flow

interface FavoritesInteractor {
    fun favoritesTracks() : Flow<List<Track>>
}