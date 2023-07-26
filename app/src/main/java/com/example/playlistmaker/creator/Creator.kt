package com.example.playlistmaker.creator

import android.app.Application
import android.content.Context
import com.example.playlistmaker.data.player.PlayerRepositoryImpl
import com.example.playlistmaker.domain.player.PlayerInteractor
import com.example.playlistmaker.domain.player.PlayerInteractorImpl
import com.example.playlistmaker.data.player.PlayerRepository
import com.example.playlistmaker.data.search.network.retrofit.RetrofitNetworkClient
import com.example.playlistmaker.data.search.network.retrofit.TrackRepository
import com.example.playlistmaker.data.search.network.retrofit.TrackRepositoryImpl
import com.example.playlistmaker.data.search.sharedprefs.HistoryRepository
import com.example.playlistmaker.data.search.sharedprefs.impl.HistoryRepositoryImpl
import com.example.playlistmaker.data.settings.sharedprefs.ThemeRepository
import com.example.playlistmaker.data.settings.sharedprefs.impl.ThemeRepositoryImpl
import com.example.playlistmaker.data.sharing.ExternalNavigatorRepository
import com.example.playlistmaker.data.sharing.impl.ExternalNavigatorRepositoryImpl
import com.example.playlistmaker.domain.search.TrackInteractor
import com.example.playlistmaker.domain.search.impl.TrackInteractorImpl
import com.example.playlistmaker.domain.settings.sharedprefs.HistoryInteractor
import com.example.playlistmaker.domain.settings.sharedprefs.impl.HistoryInteractorImpl
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

    fun provideThemeInteractor(context: Context): ThemeInteractorImpl? {
        return if (context is Application) {
            ThemeInteractorImpl(getThemeRepository(context = context))
        } else {
            null
        }
    }

    private fun getThemeRepository(context: Context): ThemeRepository {
        return ThemeRepositoryImpl(context = context)
    }

    fun provideHistoryInteractor(context: Context): HistoryInteractor {
        return HistoryInteractorImpl(getHistoryRepository(context = context))
    }

    private fun getHistoryRepository(context: Context): HistoryRepository {
        return HistoryRepositoryImpl(context = context)
    }

    fun provideExternalNavigator(context: Context): ExternalNavigatorInteractor?{
        return if (context is Application) {
            ExternalNavigatorInteractorImpl(getExternalNavigatorRepository(context = context))
        } else {
            null
        }
    }

    private fun getExternalNavigatorRepository(context: Context): ExternalNavigatorRepository{
        return ExternalNavigatorRepositoryImpl(context = context)
    }

    fun provideTrackInteractor(context: Context): TrackInteractor?{
        return if (context is Application) {
            return TrackInteractorImpl(getTrackRepository(context = context))
        } else {
            null
        }
    }

    private fun getTrackRepository(context: Context): TrackRepository{
        return TrackRepositoryImpl(RetrofitNetworkClient(context = context))
    }
}