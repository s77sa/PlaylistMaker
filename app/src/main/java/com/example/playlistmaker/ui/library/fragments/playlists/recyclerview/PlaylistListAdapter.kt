package com.example.playlistmaker.ui.library.fragments.playlists.recyclerview

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.R
import com.example.playlistmaker.data.models.Playlist
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class PlaylistListAdapter(
    private val data: MutableList<Playlist>,
    private val coroutineScope: CoroutineScope,
    private val layoutResItem: Int
) :
    RecyclerView.Adapter<PlaylistListViewHolder>() {

    private var isClickAllowed = true
    private var onClickJob: Job? = null
    private var onClickListener: OnClickListener? = null


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaylistListViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(layoutResItem, parent, false)
            //LayoutInflater.from(parent.context).inflate(R.layout.playlist_item, parent, false)
        return PlaylistListViewHolder(view)
    }

    override fun onBindViewHolder(holder: PlaylistListViewHolder, position: Int) {
        val item: Playlist = data[position]
        holder.bind(item)
        holder.itemView.setOnClickListener {
            if (onClickListener != null && clickDebounce()) {
                onClickListener!!.onClick(position, item)
            }
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }

    fun setOnClickListener(onClickListener: OnClickListener) {
        this.onClickListener = onClickListener
    }

    interface OnClickListener {
        fun onClick(position: Int, track: Playlist)
    }

    private fun clickDebounce(): Boolean {
        onClickJob?.cancel()
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            onClickJob = coroutineScope.launch {
                delay(CLICK_DEBOUNCE_DELAY)
                isClickAllowed = true
            }
        }
        return current
    }

    companion object {
        private const val CLICK_DEBOUNCE_DELAY = 1000L
    }
}