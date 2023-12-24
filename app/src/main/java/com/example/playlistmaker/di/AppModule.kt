package com.example.playlistmaker.di

import com.example.playlistmaker.data.models.Track
import com.example.playlistmaker.ui.library.fragments.favorites.FavoritesFragmentViewModel
import com.example.playlistmaker.ui.library.fragments.playlists.PlaylistsFragmentViewModel
import com.example.playlistmaker.ui.library.fragments.playlists.create.CreatePlaylistFragmentViewModel
import com.example.playlistmaker.ui.player.PlayerFragmentViewModel
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
            historyInteractor = get()
        )
    }

    viewModel<PlayerFragmentViewModel> { (track: Track) ->
        PlayerFragmentViewModel(
            track = track,
            get(),
            get()
        )
    }

    viewModel<FavoritesFragmentViewModel> {
        FavoritesFragmentViewModel(
            get()
        )
    }

    viewModel<PlaylistsFragmentViewModel> {
        PlaylistsFragmentViewModel(get())
    }

    single<ActivityNavigator> {
        ActivityNavigator(get())
    }

    viewModel<CreatePlaylistFragmentViewModel> {
        CreatePlaylistFragmentViewModel(get(), get())
    }
}