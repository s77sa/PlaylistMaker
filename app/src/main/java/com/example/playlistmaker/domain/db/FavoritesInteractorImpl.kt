package com.example.playlistmaker.domain.db

import com.example.playlistmaker.data.search.models.Track
import kotlinx.coroutines.flow.Flow

class FavoritesInteractorImpl(
    private val favoritesRepository: FavoritesRepository
) : FavoritesInteractor{
    override fun favoritesTracks(): Flow<List<Track>> {
        return  favoritesRepository.favoritesTracks()
    }
}