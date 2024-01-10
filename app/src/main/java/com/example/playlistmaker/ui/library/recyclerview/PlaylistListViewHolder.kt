package com.example.playlistmaker.ui.library.recyclerview

import android.net.Uri
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.R
import com.example.playlistmaker.domain.model.Playlist
import com.example.playlistmaker.ui.utils.Helpers

class PlaylistListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val playlistArt: ImageView = itemView.findViewById(R.id.iv_playlist_art)
    private val playlistName: TextView = itemView.findViewById(R.id.tv_playlist_name)
    private val playlistTracksCount: TextView =
        itemView.findViewById(R.id.tv_tracks_in_playlist_count)
    private val playlistTracksText: TextView = itemView.findViewById(R.id.tv_text_tracks)

    fun bind(item: Playlist) {
        if (!item.imagePath.isNullOrEmpty()) {
            playlistArt.setImageURI(getUri(item.imagePath))
        }
        playlistName.text = item.name
        playlistTracksCount.text = item.tracksCount.toString()
        playlistTracksText.text =
            Helpers.tracksDeclension(playlistTracksText.context, item.tracksCount)
    }

    private fun getUri(fileName: String): Uri {
        return Uri.parse(fileName)
    }

    companion object
}