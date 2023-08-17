package com.example.playlistmaker.ui.library.fragments

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class FragmentFavoritesViewModel : ViewModel() {
    private val isMutableEditing = MutableLiveData<Boolean>().apply { false }
    val isEditing: LiveData<Boolean> = isMutableEditing

}