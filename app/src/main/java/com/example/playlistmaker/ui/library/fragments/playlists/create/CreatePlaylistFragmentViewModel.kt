package com.example.playlistmaker.ui.library.fragments.playlists.create

import android.net.Uri
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.data.db.AppDatabase
import com.example.playlistmaker.data.db.converters.PlaylistDbConvertor
import com.example.playlistmaker.data.models.Playlist
import com.example.playlistmaker.data.privatestorage.PrivateStorage
import kotlinx.coroutines.launch


class CreatePlaylistFragmentViewModel(
    private val privateStorage: PrivateStorage,
    private val appDatabase: AppDatabase,
    private val playlistDbConvertor: PlaylistDbConvertor
) : ViewModel() {

    private val mutableFileUri = MutableLiveData<Uri?>().apply {  }
    val fileUri get() = mutableFileUri

    private val mutablePlaylistName = MutableLiveData<String>().apply { }
    val playlistName get() = mutablePlaylistName

    private val mutablePlaylistDescription = MutableLiveData<String>().apply { }
    val playlistDescription get() = mutablePlaylistDescription

    fun savePlaylist() {
        val playlist: Playlist = Playlist(
            0,
            mutablePlaylistName.value!!,
            mutablePlaylistDescription.value,
            mutableFileUri.value?.path,
            0
        )
        playlistDbConvertor.map(playlist)
        viewModelScope.launch {
            appDatabase.playlistsDao().addPlaylist(playlistDbConvertor.map(playlist))
        }
    }

    fun setPlayListName(text: String) {
        mutablePlaylistName.value = text
    }

    fun setPlaylistDescription(text: String) {
        mutablePlaylistDescription.value = text
    }

    fun saveImageToPrivateStorage(uri: Uri) {
        Log.d(TAG, uri.path.toString())
        val fileName = privateStorage.saveImage(uri)
        mutableFileUri.value = loadImageToPrivateStorage(fileName)

    }

    private fun loadImageToPrivateStorage(fileName: String): Uri {
        return privateStorage.loadImage(fileName)
    }

    private fun checkPermission(): Boolean {
        return true
    }

    companion object {
        private val TAG = CreatePlaylistFragmentViewModel::class.simpleName
    }

}