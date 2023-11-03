package com.example.playlistmaker.di

import com.example.playlistmaker.data.search.models.Track
import com.example.playlistmaker.ui.library.fragments.FragmentFavoritesViewModel
import com.example.playlistmaker.ui.library.fragments.FragmentPlaylistsViewModel
import com.example.playlistmaker.ui.player.PlayerViewModel
import com.example.playlistmaker.ui.root.RootViewModel
import com.example.playlistmaker.ui.search.SearchFragmentViewModel
import com.example.playlistmaker.ui.settings.SettingsFragmentViewModel
import com.example.playlistmaker.ui.sharing.ActivityNavigator
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    viewModel<RootViewModel> {
        RootViewModel(
            themeInteractor = get()
        )
    }

    viewModel<SettingsFragmentViewModel> {
        SettingsFragmentViewModel(
            themeInteractor = get(),
            externalNavigator = get()
        )
    }

    viewModel<SearchFragmentViewModel> {
        SearchFragmentViewModel(
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