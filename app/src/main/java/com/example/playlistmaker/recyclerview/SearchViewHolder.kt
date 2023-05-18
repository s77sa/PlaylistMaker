package com.example.playlistmaker.recyclerview

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.R
import com.example.playlistmaker.models.Utils
import com.example.playlistmaker.retrofit.Track

class SearchViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val artWork: ImageView = itemView.findViewById((R.id.iv_artWork))
    private val trackName: TextView = itemView.findViewById((R.id.tv_trackName))
    private val artistName: TextView = itemView.findViewById((R.id.tv_artistName))
    private val trackTime: TextView = itemView.findViewById((R.id.tv_trackTime))
    private val imageViewCorners: Int = 2

    fun bind(item: Track) {
        trackName.text = item.trackName
        artistName.text = item.artistName
        trackTime.text = Utils.millisToString(item.trackTimeMillis)
        Utils.glideBind(item.artworkUrl100, artWork, imageViewCorners)

    }
}