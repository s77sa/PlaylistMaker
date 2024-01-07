package com.example.playlistmaker.ui.library.fragments.playlists.create

import android.net.Uri
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.data.privatestorage.PrivateStorage
import com.example.playlistmaker.domain.db.DbInteractor
import com.example.playlistmaker.domain.model.Playlist
import kotlinx.coroutines.launch


open class CreatePlaylistFragmentViewModel(
    private val privateStorage: PrivateStorage,
    val dbInteractor: DbInteractor
) : ViewModel() {

    private val mutableFileUri = MutableLiveData<Uri?>().apply { }
    open val fileUri get() = mutableFileUri

    private val mutablePlaylistName = MutableLiveData<String>().apply { }
    open val playlistName get() = mutablePlaylistName

    private val mutablePlaylistDescription = MutableLiveData<String>().apply { }
    open val playlistDescription get() = mutablePlaylistDescription

    open fun savePlaylist() {
        val playlist = Playlist(
            0,
            mutablePlaylistName.value!!,
            mutablePlaylistDescription.value,
            mutableFileUri.value?.path,
            0
        )
        viewModelScope.launch {
            dbInteractor.addPlaylist(playlist)
        }
    }

    open fun setPlayListName(text: String) {
        mutablePlaylistName.value = text
    }

    open fun setPlaylistDescription(text: String) {
        mutablePlaylistDescription.value = text
    }

    fun saveImageToPrivateStorage(uri: Uri) {
        Log.d(TAG, uri.path.toString())
        val fileName = privateStorage.saveImage(uri)
        val fullPath = loadImageFromPrivateStorage(fileName)
        mutableFileUri.value = fullPath
    }

    private fun loadImageFromPrivateStorage(fileName: String): Uri {
        return privateStorage.loadImage(fileName)
    }

    companion object {
        val TAG = CreatePlaylistFragmentViewModel::class.simpleName
    }

}