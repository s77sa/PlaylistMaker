package com.example.playlistmaker.ui.library.fragments.playlists

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel


class PlaylistsFragmentViewModel : ViewModel() {
    private val isMutableEditing = MutableLiveData<Boolean>().apply { false }
    val isEditing: LiveData<Boolean> = isMutableEditing
}