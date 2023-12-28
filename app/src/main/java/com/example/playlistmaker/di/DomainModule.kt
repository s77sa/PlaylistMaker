package com.example.playlistmaker.di

import com.example.playlistmaker.domain.db.DbInteractor
import com.example.playlistmaker.domain.db.impl.DbInteractorImpl
import com.example.playlistmaker.domain.db.DbRepository
import com.example.playlistmaker.data.db.impl.DbRepositoryImpl
import com.example.playlistmaker.domain.player.PlayerInteractor
import com.example.playlistmaker.domain.player.impl.PlayerInteractorImpl
import com.example.playlistmaker.domain.search.TrackInteractor
import com.example.playlistmaker.domain.search.impl.TrackInteractorImpl
import com.example.playlistmaker.domain.settings.sharedprefs.HistoryInteractor
import com.example.playlistmaker.domain.settings.sharedprefs.ThemeInteractor
import com.example.playlistmaker.domain.settings.sharedprefs.impl.HistoryInteractorImpl
import com.example.playlistmaker.domain.settings.sharedprefs.impl.ThemeInteractorImpl
import com.example.playlistmaker.domain.sharing.ExternalNavigatorInteractor
import com.example.playlistmaker.domain.sharing.impl.ExternalNavigatorInteractorImpl
import org.koin.dsl.module

val domainModule = module {
    single<ThemeInteractor> {
        ThemeInteractorImpl(repository = get())
    }

    single<ExternalNavigatorInteractor> {
        ExternalNavigatorInteractorImpl(repository = get())
    }

    single<HistoryInteractor>{
        HistoryInteractorImpl(repository = get())
    }

    single<TrackInteractor>{
        TrackInteractorImpl(repository = get())
    }

    single<PlayerInteractor>{
        PlayerInteractorImpl(repository = get())
    }

    single<DbRepository> {
        DbRepositoryImpl(get(), get(), get(), get())
    }

    single<DbInteractor> {
        DbInteractorImpl(get())
    }
}