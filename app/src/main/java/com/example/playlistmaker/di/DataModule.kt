package com.example.playlistmaker.di

import com.example.playlistmaker.data.player.PlayerRepository
import com.example.playlistmaker.data.player.PlayerRepositoryImpl
import com.example.playlistmaker.data.search.gson.GsonRepository
import com.example.playlistmaker.data.search.gson.impl.GsonRepositoryImpl
import com.example.playlistmaker.data.search.network.retrofit.InterceptorClientRepository
import com.example.playlistmaker.data.search.network.retrofit.RetrofitNetworkClient
import com.example.playlistmaker.data.search.network.retrofit.RetrofitRepository
import com.example.playlistmaker.data.search.network.retrofit.TrackRepository
import com.example.playlistmaker.data.search.network.retrofit.impl.InterceptorClientRepositoryImpl
import com.example.playlistmaker.data.search.network.retrofit.impl.RetrofitRepositoryImpl
import com.example.playlistmaker.data.search.network.retrofit.impl.TrackRepositoryImpl
import com.example.playlistmaker.data.search.sharedprefs.HistoryRepository
import com.example.playlistmaker.data.search.sharedprefs.impl.HistoryRepositoryImpl
import com.example.playlistmaker.data.settings.sharedprefs.ThemeRepository
import com.example.playlistmaker.data.settings.sharedprefs.impl.ThemeRepositoryImpl
import com.example.playlistmaker.data.sharing.ExternalNavigatorRepository
import com.example.playlistmaker.data.sharing.SharedPreferencesRepository
import com.example.playlistmaker.data.sharing.impl.ExternalNavigatorRepositoryImpl
import com.example.playlistmaker.data.sharing.impl.SharedPreferencesRepositoryImpl
import org.koin.dsl.module

val dataModule = module {
    single<SharedPreferencesRepository> {
        SharedPreferencesRepositoryImpl(context = get())
    }

    single<ThemeRepository> {
        ThemeRepositoryImpl(sharedPrefsRepository = get())
    }

    single<ExternalNavigatorRepository> {
        ExternalNavigatorRepositoryImpl(context = get())
    }

    single<HistoryRepository> {
        HistoryRepositoryImpl(
            sharedPrefsRepository = get(),
            gsonRepository = get()
        )
    }

    single<GsonRepository> {
        GsonRepositoryImpl()
    }

    single<TrackRepository> {
        TrackRepositoryImpl(RetrofitNetworkClient(context = get(), retrofitRepository = get()))
    }

    single<RetrofitRepository>{
        RetrofitRepositoryImpl(get(), interceptorClientRepository = get())
    }

    single<InterceptorClientRepository>{
        InterceptorClientRepositoryImpl()
    }

    single<PlayerRepository> {
        PlayerRepositoryImpl()
    }
}