package com.example.playlistmaker.ui.library.fragments.favorites

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.data.search.models.Track
import com.example.playlistmaker.domain.db.FavoritesInteractor
import com.example.playlistmaker.ui.player.KEY_INTENT_PLAYER_ACTIVITY
import com.example.playlistmaker.ui.player.PlayerActivity
import com.example.playlistmaker.ui.sharing.ActivityNavigator
import kotlinx.coroutines.launch


class FragmentFavoritesViewModel(
    private val activityNavigator: ActivityNavigator,
    private val favoritesInteractor: FavoritesInteractor
) : ViewModel() {
    init {
        loadFavoriteTracks()
    }

    private var trackListMutable = MutableLiveData<List<Track>>()
    val trackList: LiveData<List<Track>> = trackListMutable

    private fun loadFavoriteTracks() {
        viewModelScope.launch {
            favoritesInteractor
                .favoritesTracks()
                .collect { tracks -> processResult(tracks) }
        }
    }

    private fun processResult(tracks: List<Track>) {
        trackListMutable.value = tracks
    }

    @Suppress("UNCHECKED_CAST")
    fun callPlayerActivity(item: Track) {
        activityNavigator.intentCallWithKeySerializable(
            PlayerActivity::class.java as Class<Any>,
            KEY_INTENT_PLAYER_ACTIVITY,
            item
        )
    }

}