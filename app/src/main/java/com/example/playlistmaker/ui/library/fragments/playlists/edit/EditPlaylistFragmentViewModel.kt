package com.example.playlistmaker.ui.library.fragments.playlists.edit

import android.net.Uri
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.domain.model.Playlist
import com.example.playlistmaker.data.privatestorage.PrivateStorage
import com.example.playlistmaker.domain.db.DbInteractor
import kotlinx.coroutines.launch


class EditPlaylistFragmentViewModel(
    private val privateStorage: PrivateStorage,
    private val dbInteractor: DbInteractor
) : ViewModel() {

    private val mutableFileUri = MutableLiveData<Uri?>().apply {  }
    val fileUri get() = mutableFileUri

    private val mutablePlaylistName = MutableLiveData<String>().apply { }
    val playlistName get() = mutablePlaylistName

    private val mutablePlaylistDescription = MutableLiveData<String>().apply { }
    val playlistDescription get() = mutablePlaylistDescription

}