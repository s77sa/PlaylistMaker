package com.example.playlistmaker.creator

import android.content.Context
import com.example.playlistmaker.data.player.PlayerRepositoryImpl
import com.example.playlistmaker.domain.player.PlayerInteractor
import com.example.playlistmaker.domain.player.PlayerInteractorImpl
import com.example.playlistmaker.data.player.PlayerRepository
import com.example.playlistmaker.data.settings.sharedprefs.ThemeRepository
import com.example.playlistmaker.data.settings.sharedprefs.impl.ThemeRepositoryImpl
import com.example.playlistmaker.data.sharing.ExternalNavigatorRepository
import com.example.playlistmaker.data.sharing.impl.ExternalNavigatorRepositoryImpl
import com.example.playlistmaker.domain.settings.sharedprefs.ThemeInteractor
import com.example.playlistmaker.domain.settings.sharedprefs.impl.ThemeInteractorImpl
import com.example.playlistmaker.domain.sharing.ExternalNavigatorInteractor
import com.example.playlistmaker.domain.sharing.impl.ExternalNavigatorInteractorImpl

object Creator {
    fun providePlayerInteractor(): PlayerInteractor {
        return PlayerInteractorImpl(getPlayerRepository())
    }

    private fun getPlayerRepository(): PlayerRepository {
        return PlayerRepositoryImpl()
    }

    fun provideThemeInteractor(context: Context): ThemeInteractor {
        return ThemeInteractorImpl(getThemeRepository(context = context))
    }

    private fun getThemeRepository(context: Context): ThemeRepository {
        return ThemeRepositoryImpl(context = context)
    }

    fun provideExternalNavigator(context: Context): ExternalNavigatorInteractor{
        return ExternalNavigatorInteractorImpl(getExternalNavigatorRepository(context = context))
    }

    private fun getExternalNavigatorRepository(context: Context): ExternalNavigatorRepository{
        return ExternalNavigatorRepositoryImpl(context = context)
    }

}