package com.example.playlistmaker.ui.library.fragments.playlists.recyclerview

import android.net.Uri
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat.getString
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.R
import com.example.playlistmaker.data.models.Playlist

class PlaylistListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val playlistArt: ImageView = itemView.findViewById(R.id.iv_playlist_art)
    private val playlistName: TextView = itemView.findViewById(R.id.tv_playlist_name)
    private val playlistTracksCount: TextView = itemView.findViewById(R.id.tv_playlist_count)

    fun bind(item: Playlist) {
        if (!item.imagePath.isNullOrEmpty()) {
            playlistArt.setImageURI(getUri(item.imagePath))
        }
        playlistName.text = item.name
        playlistTracksCount.text =
            getString(itemView.context, R.string.tv_playlist_tracks_count).replace(
                TRACKS_COUNT_PATTERNS,
                item.tracksCount.toString()
            )
    }

    private fun getUri(fileName: String): Uri {
        return Uri.parse(fileName)
    }

    companion object {
        private const val TRACKS_COUNT_PATTERNS = "[count]"
    }
}