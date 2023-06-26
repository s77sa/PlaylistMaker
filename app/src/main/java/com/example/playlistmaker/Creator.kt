package com.example.playlistmaker

import com.example.playlistmaker.data.player.PlayerRepositoryImpl
import com.example.playlistmaker.domain.api.PlayerInteractor
import com.example.playlistmaker.domain.impl.PlayerInteractorImpl
import com.example.playlistmaker.domain.player.PlayerRepository

object Creator {
    fun providePlayerInteractor(): PlayerInteractor{
        return PlayerInteractorImpl(
            getPlayerRepository()
        )
    }

    private fun getPlayerRepository(): PlayerRepository{
        return PlayerRepositoryImpl()
    }
}