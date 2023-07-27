package com.example.playlistmaker.ui.search.recyclerview

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.R
import com.example.playlistmaker.ui.Helpers
import com.example.playlistmaker.data.search.models.Track

class SearchViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val artWork: ImageView = itemView.findViewById((R.id.iv_artWork))
    private val trackName: TextView = itemView.findViewById((R.id.tv_trackName))
    private val artistName: TextView = itemView.findViewById((R.id.tv_artistName))
    private val trackTime: TextView = itemView.findViewById((R.id.tv_trackTime))

    fun bind(item: Track) {
        trackName.text = item.trackName
        artistName.text = item.artistName
        trackTime.text = item.trackTimeMillis?.let { Helpers.millisToString(it) }
        item.artworkUrl100?.let { Helpers.glideBind(it, artWork) }
    }
}