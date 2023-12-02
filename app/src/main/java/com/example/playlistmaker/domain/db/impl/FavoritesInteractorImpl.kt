package com.example.playlistmaker.domain.db.impl

import com.example.playlistmaker.data.search.models.Track
import com.example.playlistmaker.domain.db.FavoritesInteractor
import com.example.playlistmaker.domain.db.FavoritesRepository
import kotlinx.coroutines.flow.Flow

class FavoritesInteractorImpl(
    private val favoritesRepository: FavoritesRepository
) : FavoritesInteractor {
    override fun favoritesTracks(): Flow<List<Track>> {
        return  favoritesRepository.favoritesTracks()
    }
}