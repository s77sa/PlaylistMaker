package com.example.playlistmaker.ui.library.recyclerview

import android.net.Uri
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat.getString
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.R
import com.example.playlistmaker.domain.model.Playlist
import java.util.Locale

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
        playlistTracksText.text = numeralDeclens(item.tracksCount)
    }

    private fun getUri(fileName: String): Uri {
        return Uri.parse(fileName)
    }

    private fun numeralDeclens(count: Int): String {
        var text: String = getString(itemView.context, R.string.tv_playlist_tracks_text)
        val lastInt = (count.toString()[count.toString().length - 1]).digitToInt()
        if (Locale.getDefault().language == Locale("ru").language) {
            text = when (lastInt) {
                1 -> {
                    text.removeRange(4, text.length)
                }

                in 2..4 -> {
                    text.removeRange(5, text.length)
                }

                else -> {
                    text.removeRange(4, text.length - 2)
                }
            }
        } else {
            if (lastInt == 1) {
                text = text.removeRange(5, text.length)
            }
        }
        return text
    }

    companion object
}