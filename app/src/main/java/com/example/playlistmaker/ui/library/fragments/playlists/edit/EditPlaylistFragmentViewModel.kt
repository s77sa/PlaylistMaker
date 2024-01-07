package com.example.playlistmaker.ui.library.fragments.playlists.edit

import android.net.Uri
import android.util.Log
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.data.privatestorage.PrivateStorage
import com.example.playlistmaker.domain.db.DbInteractor
import com.example.playlistmaker.domain.model.Playlist
import com.example.playlistmaker.ui.library.fragments.playlists.create.CreatePlaylistFragmentViewModel
import kotlinx.coroutines.launch

class EditPlaylistFragmentViewModel(privateStorage: PrivateStorage, dbInteractor: DbInteractor) :
    CreatePlaylistFragmentViewModel(
        privateStorage, dbInteractor
    ) {

    private var playlistId: Int = 0
    private var playlistUri: String? = null

    override fun savePlaylist() {

        val playlist: Playlist? = super.playlistName.value?.let { name ->
            Playlist(
                playlistId,
                name,
                super.playlistDescription.value,
                super.fileUri.value?.path,
                0
            )
        }
        Log.d(TAG, "override savePlaylist ${playlist.toString()}")
        viewModelScope.launch {
            if (playlist != null) {
                dbInteractor.updatePlaylist(playlist)
            }
        }
    }

    fun setPlaylistId(id: Int) {
        playlistId = id
    }

    fun setPlaylistUri(value: String) {
        playlistUri = value
    }

    fun setImageUri(stringUri: String) {
        super.fileUri.value = Uri.parse(stringUri)
    }
}