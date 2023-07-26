package com.example.playlistmaker.di

import com.example.playlistmaker.data.player.PlayerRepository
import com.example.playlistmaker.data.player.PlayerRepositoryImpl
import com.example.playlistmaker.data.search.network.retrofit.RetrofitNetworkClient
import com.example.playlistmaker.data.search.network.retrofit.TrackRepository
import com.example.playlistmaker.data.search.network.retrofit.TrackRepositoryImpl
import com.example.playlistmaker.data.search.sharedprefs.HistoryRepository
import com.example.playlistmaker.data.search.sharedprefs.impl.HistoryRepositoryImpl
import com.example.playlistmaker.data.settings.sharedprefs.ThemeRepository
import com.example.playlistmaker.data.settings.sharedprefs.impl.ThemeRepositoryImpl
import com.example.playlistmaker.data.sharing.ExternalNavigatorRepository
import com.example.playlistmaker.data.sharing.impl.ExternalNavigatorRepositoryImpl
import org.koin.dsl.module

val dataModule = module {
    single<ThemeRepository> {
        ThemeRepositoryImpl(context = get())
    }

    single<ExternalNavigatorRepository> {
        ExternalNavigatorRepositoryImpl(context = get())
    }

    single<ExternalNavigatorRepository> {
        ExternalNavigatorRepositoryImpl(context = get())
    }

    single<HistoryRepository>{
        HistoryRepositoryImpl(context = get())
    }

    single<TrackRepository>{
        TrackRepositoryImpl(RetrofitNetworkClient(context = get()))
    }

    single<PlayerRepository>{
        PlayerRepositoryImpl()
    }
}