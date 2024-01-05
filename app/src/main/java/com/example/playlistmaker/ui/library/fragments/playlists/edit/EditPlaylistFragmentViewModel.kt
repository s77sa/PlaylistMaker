package com.example.playlistmaker.ui.library.fragments.playlists.edit

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.data.privatestorage.PrivateStorage
import com.example.playlistmaker.domain.db.DbInteractor
import com.example.playlistmaker.domain.model.Playlist
import com.example.playlistmaker.domain.model.Track
import com.example.playlistmaker.ui.utils.Helpers
import kotlinx.coroutines.launch


class EditPlaylistFragmentViewModel(
    private val context: Context,
    private val privateStorage: PrivateStorage,
    private val dbInteractor: DbInteractor
) : ViewModel() {

    private val mutableFileUri = MutableLiveData<Uri?>().apply { }
    val fileUri get() = mutableFileUri

    private val mutablePlaylistName = MutableLiveData<String>().apply { }
    val playlistName get() = mutablePlaylistName

    private val mutablePlaylistDescription = MutableLiveData<String>().apply { }
    val playlistDescription get() = mutablePlaylistDescription

    private val playlistTotalMinuteMutable = MutableLiveData<String>().apply { }
    val playlistTotalMinute get() = playlistTotalMinuteMutable

    private val playlistTrackCountMutable = MutableLiveData<String>().apply { }
    val playlistTrackCount get() = playlistTrackCountMutable

    private val tracksInPlaylistMutable = MutableLiveData<List<Track>>().apply { }
    val tracksInPlaylist get() = tracksInPlaylistMutable

    private val playlistMutable = MutableLiveData<Playlist>().apply { }
    val playlist get() = playlistMutable

    fun setPlaylist(playlist: Playlist) {
        Log.d(TAG, "playlist.name = ${playlist.name}")
        playlistMutable.value = playlist
        loadPlaylistInformation(playlist)
    }

    private fun loadPlaylistInformation(playlist: Playlist) {
        getTracksInPlaylist(playlist)
    }

    private fun loadOtherPlaylistInformation(){
        mutablePlaylistName.value = playlistMutable.value?.name
        if (!playlistMutable.value?.description.isNullOrEmpty()) {
            mutablePlaylistDescription.value = playlistMutable.value?.description
        }
        if (!playlistMutable.value?.imagePath.isNullOrEmpty()) {
            mutableFileUri.value = Uri.parse(playlistMutable.value?.imagePath)
        }
        playlistTrackCountMutable.value = getTrackCount()
        playlistTotalMinuteMutable.value = getTracksTotalTime()
    }

    private fun getTrackCount(): String {
        val count = tracksInPlaylistMutable.value?.size ?: 0
        val text = Helpers.tracksDeclension(
            context, count
        )
        Log.d(TAG, "$count $text")
        return "$count $text"
    }

    private fun getTracksTotalTime(): String {
        var value = 0
        val tracks = tracksInPlaylistMutable.value
        if (!tracks.isNullOrEmpty()) {
            for (track in tracks) {
                if (track.trackTimeMillis != null) {
                    value += track.trackTimeMillis / 1000 // Millis to seconds
                }
            }
        }
        value /= 60 // Seconds to minutes
        val text = Helpers.minutesDeclension(context, value)
        return "$value $text"
    }

    private fun getTracksInPlaylist(playlist: Playlist) {
        viewModelScope.launch {
            dbInteractor
                .tracksInPlaylists(playlistId = playlist.id)
                .collect { tracks -> tracksResult(tracks) }
        }
    }

    private fun tracksResult(tracks: List<Track>) {
        tracksInPlaylistMutable.value = tracks
        loadOtherPlaylistInformation()
    }

    companion object {
        private val TAG = EditPlaylistFragmentViewModel::class.java.simpleName
    }

}