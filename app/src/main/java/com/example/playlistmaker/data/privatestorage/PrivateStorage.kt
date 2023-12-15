package com.example.playlistmaker.data.privatestorage

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Environment
import android.util.Log
import android.view.RoundedCorner
import androidx.core.net.toUri
import com.example.playlistmaker.ui.library.fragments.playlists.create.CreatePlaylistFragment
import java.io.File
import java.io.FileOutputStream
import java.util.UUID

class PrivateStorage(
    private val context: Context
) {
    fun saveImage(uri: Uri): String {
        val filePath = File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), ALBUM_NAME)
        if (!filePath.exists()) {
            filePath.mkdirs()
        }
        val fileName = generateName()
        val file = File(filePath, fileName)
        val inputStream = context.contentResolver.openInputStream(uri)
        val outputStream = FileOutputStream(file)
        BitmapFactory
            .decodeStream(inputStream)
            .compress(Bitmap.CompressFormat.JPEG, BITMAP_QUALITY, outputStream)
        Log.d(TAG, fileName)
        return fileName
    }

    fun loadImage(fileName: String): Uri {
        Log.d(TAG, fileName)
        val filePath = File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), ALBUM_NAME)
        val file = File(filePath, fileName)
        return file.toUri()
    }

    private fun generateName(): String {
        return UUID.randomUUID().toString().replace("-", "")
    }

    companion object {
        private const val ALBUM_NAME = "playlistAlbum"
        private const val BITMAP_QUALITY = 30
        private val TAG = PrivateStorage::class.simpleName
    }
}