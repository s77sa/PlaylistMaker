package com.example.playlistmaker.di

import com.example.playlistmaker.data.models.Track
import com.example.playlistmaker.ui.library.fragments.favorites.FragmentFavoritesViewModel
import com.example.playlistmaker.ui.library.fragments.playlists.PlaylistsFragmentViewModel
import com.example.playlistmaker.ui.player.PlayerViewModel
import com.example.playlistmaker.ui.library.fragments.playlists.create.CreatePlaylistFragmentViewModel
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

    viewModel<PlayerViewModel> { (track: Track) ->
        PlayerViewModel(
            track = track,
            get(),
            get(),
            get()
        )
    }

    viewModel<FragmentFavoritesViewModel> {
        FragmentFavoritesViewModel(
            get(),
            get()
        )
    }

    viewModel<PlaylistsFragmentViewModel> {
        PlaylistsFragmentViewModel()
    }

    single<ActivityNavigator> {
        ActivityNavigator(get())
    }

    viewModel<CreatePlaylistFragmentViewModel>{
        CreatePlaylistFragmentViewModel(get(), get(), get())
    }
}