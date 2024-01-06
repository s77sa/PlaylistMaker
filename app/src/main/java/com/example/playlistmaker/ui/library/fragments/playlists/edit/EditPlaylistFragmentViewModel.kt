package com.example.playlistmaker.ui.library.fragments.playlists.edit

import android.util.Log
import com.example.playlistmaker.data.privatestorage.PrivateStorage
import com.example.playlistmaker.domain.db.DbInteractor
import com.example.playlistmaker.ui.library.fragments.playlists.create.CreatePlaylistFragmentViewModel

class EditPlaylistFragmentViewModel(privateStorage: PrivateStorage, dbInteractor: DbInteractor) :
    CreatePlaylistFragmentViewModel(
        privateStorage, dbInteractor
    ) {

    override fun savePlaylist() {
        //super.savePlaylist()
        //Log.d(TAG, "override save")
    }
}