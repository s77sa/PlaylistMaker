package com.example.playlistmaker.ui.library.fragments.playlists.create

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.playlistmaker.data.privatestorage.PrivateStorage
import com.example.playlistmaker.data.search.models.Track
import com.example.playlistmaker.ui.root.RootActivity


class CreatePlaylistFragmentViewModel(
    private val context: Context,
    private val privateStorage: PrivateStorage
) : ViewModel() {
    init {

    }

//    private val searchTextMutable = MutableLiveData<String>().apply { }
//    val searchText: LiveData<String> = searchTextMutable

    private val mutableFileUri = MutableLiveData<Uri>().apply {}
    val fileUri get() = mutableFileUri


    fun initSelectImage(){
        if (checkPermission()){
            getImage()
        }
    }

    private fun getImage(){

    }

    fun saveImageToPrivateStorage(uri: Uri){
        Log.d(TAG, uri.path.toString())
        val fileName = privateStorage.saveImage(uri)
        mutableFileUri.value = loadImageToPrivateStorage(fileName)

    }

    private fun loadImageToPrivateStorage(fileName: String): Uri {
        return privateStorage.loadImage(fileName)
    }

    private fun checkPermission() : Boolean{
        return true
    }

    companion object {
        private val TAG = CreatePlaylistFragmentViewModel::class.simpleName
    }

}