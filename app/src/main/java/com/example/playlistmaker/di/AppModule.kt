package com.example.playlistmaker.di

import com.example.playlistmaker.data.search.models.Track
import com.example.playlistmaker.ui.library.fragments.FragmentFavoritesViewModel
import com.example.playlistmaker.ui.library.fragments.FragmentPlaylistsViewModel
import com.example.playlistmaker.ui.main.MainViewModel
import com.example.playlistmaker.ui.player.PlayerViewModel
import com.example.playlistmaker.ui.search.SearchViewModel
import com.example.playlistmaker.ui.settings.SettingsViewModel
import com.example.playlistmaker.ui.sharing.ActivityNavigator
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    viewModel<MainViewModel> {
        MainViewModel(
            themeInteractor = get(),
            activityNavigator = get()
        )
    }

    viewModel<SettingsViewModel> {
        SettingsViewModel(
            themeInteractor = get(),
            externalNavigator = get()
        )
    }

    viewModel<SearchViewModel> {
        SearchViewModel(
            trackInteractor = get(),
            activityNavigator = get(),
            historyInteractor = get()
        )
    }

    viewModel<PlayerViewModel> {(track: Track) ->
        PlayerViewModel(
            track = track, get()
        )
    }

    viewModel<FragmentFavoritesViewModel> {
        FragmentFavoritesViewModel()
    }

    viewModel<FragmentPlaylistsViewModel> {
        FragmentPlaylistsViewModel()
    }

    single<ActivityNavigator> {
        ActivityNavigator(get())
    }
}