package com.example.playlistmaker.ui.library.fragments.playlists.create

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.playlistmaker.data.privatestorage.PrivateStorage


class CreatePlaylistFragmentViewModel(
    private val context: Context,
    private val privateStorage: PrivateStorage
) : ViewModel() {

    private val mutableFileUri = MutableLiveData<Uri>().apply { }
    val fileUri get() = mutableFileUri

    private val mutablePlaylistName = MutableLiveData<String>().apply { }
    val playlistName get() = mutablePlaylistName

    private val mutablePlaylistDescription = MutableLiveData<String>().apply { }
    val playlistDescription get() = mutablePlaylistDescription

//    fun initSelectImage() {
//        if (checkPermission()) {
//            getImage()
//        }
//    }
//
//    private fun getImage() {
//
//    }
    fun savePlaylist(){

    }

    fun setPlayListName(text: String)
    {
        mutablePlaylistName.value = text
    }

    fun setPlaylistDescription(text: String){
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