package com.example.playlistmaker.ui.library.fragments.favorites

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.data.models.Track
import com.example.playlistmaker.domain.db.DbInteractor
import com.example.playlistmaker.ui.sharing.ActivityNavigator
import kotlinx.coroutines.launch


class FavoritesFragmentViewModel(
    private val dbInteractor: DbInteractor
) : ViewModel() {

    private var trackListMutable = MutableLiveData<List<Track>>()
    val trackList: LiveData<List<Track>> = trackListMutable

    fun loadFavoriteTracks() {
        viewModelScope.launch {
            dbInteractor
                .favoritesTracks()
                .collect { tracks -> processResult(tracks) }
        }
    }

    private fun processResult(tracks: List<Track>) {
        trackListMutable.value = tracks
    }
}