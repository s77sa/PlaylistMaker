package com.example.playlistmaker.ui.library.fragments.playlists

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.data.models.Playlist
import com.example.playlistmaker.domain.db.DbInteractor
import kotlinx.coroutines.launch


class PlaylistsFragmentViewModel(
    private val dbInteractor: DbInteractor
) : ViewModel() {

    private var playlistListMutable = MutableLiveData<List<Playlist>>()
    val playlistList: LiveData<List<Playlist>> = playlistListMutable

    fun getPlaylists() {
        viewModelScope.launch {
            dbInteractor
                .playlists()
                .collect { playlist -> playlistResult(playlist) }
        }
    }

    private fun playlistResult(playlists: List<Playlist>) {
        for (playlist in playlists){
            playlist.tracksCount = getCountTracks(playlist.id)
        }
        playlistListMutable.value = playlists
    }

    private fun getCountTracks(trackId: Int): Int{
        var count = 0
        viewModelScope.launch{
            count = dbInteractor.countTracksInPlaylists(trackId)
        }
        return count
    }
}