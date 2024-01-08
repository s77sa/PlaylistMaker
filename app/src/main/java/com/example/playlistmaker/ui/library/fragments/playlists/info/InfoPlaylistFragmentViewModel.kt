package com.example.playlistmaker.ui.library.fragments.playlists.info

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.domain.db.DbInteractor
import com.example.playlistmaker.domain.model.Playlist
import com.example.playlistmaker.domain.model.Track
import com.example.playlistmaker.domain.sharing.ExternalNavigatorInteractor
import com.example.playlistmaker.ui.utils.Helpers
import kotlinx.coroutines.launch


class InfoPlaylistFragmentViewModel(
    private val context: Context,
    private val externalNavigatorInteractor: ExternalNavigatorInteractor,
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

    fun initDeletePlaylist() {
        val playlistId: Int = playlist.value?.id ?: -1
        callDeletePlaylist(playlistId)
        callDeleteAllTracks(playlistId)
    }

    private fun callDeletePlaylist(playlistId: Int) {
        viewModelScope.launch {
            dbInteractor.deletePlaylist(playlistId)
        }
    }

    private fun callDeleteAllTracks(playlistId: Int) {
        viewModelScope.launch {
            dbInteractor.deleteAllTrackInPlaylist(playlistId)
        }
    }

    fun callSharePlaylist() {
        externalNavigatorInteractor.intentSend(generateSendMessage())
    }

    private fun generateSendMessage(): String {
        var message: String? = null
        message = "${playlistMutable.value?.name}"
        if (!playlistMutable.value?.description.isNullOrEmpty()) {
            message += "\n${playlistMutable.value?.description}"
        }
        val tracks = tracksInPlaylistMutable.value
        var count = 0
        if (tracks != null) {
            for (track in tracks) {
                count++
                val duration = track.trackTimeMillis?.let { Helpers.millisToString(it) }
                message += "\n${count}. ${track.artistName} - ${track.trackName} ($duration)"
            }
        }
        Log.d(TAG, message)
        return message
    }

    fun setPlaylist(playlist: Playlist) {
        Log.d(TAG, "playlist.name = ${playlist.name}")
        playlistMutable.value = playlist
        loadPlaylistInformation(playlist)
    }

    fun callDeleteTrack(track: Track) {
        val playlist = playlistMutable.value
        Log.d(TAG, "Init drop track: ${track.trackName}")
        viewModelScope.launch {
            if (playlist != null) {
                track.trackId?.let { dbInteractor.deleteTrackFromPlaylist(playlist.id, it) }
                loadPlaylistInformation(playlist)
            }
        }
    }

    private fun loadPlaylistInformation(playlist: Playlist) {
        getTracksInPlaylist(playlist)
    }

    private fun loadOtherPlaylistInformation() {
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
        Log.d(TAG, "getTracksInPlaylist")
        viewModelScope.launch {
            dbInteractor
                .tracksInPlaylists(playlistId = playlist.id)
                .collect { tracks ->
                    tracksResult(tracks)
                    Log.d(TAG, "getTracksInPlaylist  coroutine: ${tracks.size}")
                }
        }
    }

    private fun tracksResult(tracks: List<Track>) {
        Log.d(TAG, "tracksResult: ${tracks.size}")
        tracksInPlaylistMutable.value = tracks
        loadOtherPlaylistInformation()
    }

    companion object {
        private val TAG = InfoPlaylistFragmentViewModel::class.java.simpleName
    }

}