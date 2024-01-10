package com.example.playlistmaker.ui.search.recyclerview

import android.view.LayoutInflater
import android.view.View
import android.view.View.OnLongClickListener
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.R
import com.example.playlistmaker.domain.model.Track
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class TrackListAdapter(
    private val data: MutableList<Track>,
    private val coroutineScope: CoroutineScope
) :
    RecyclerView.Adapter<TrackListViewHolder>() {
    companion object {
        private const val CLICK_DEBOUNCE_DELAY = 1000L
    }

    private var isClickAllowed = true
    private var onClickJob: Job? = null
    private var onClickListener: OnClickListener? = null
    private var onLongClickListener: OnLongClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackListViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.search_item, parent, false)
        return TrackListViewHolder(view)
    }

    override fun onBindViewHolder(holder: TrackListViewHolder, position: Int) {
        val item: Track = data[position]
        holder.bind(item)
        holder.itemView.setOnClickListener {
            if (onClickListener != null && clickDebounce()) {
                onClickListener?.onClick(position, item)
            }
        }
        holder.itemView.setOnLongClickListener{
            onLongClickListener?.onLongClick(position,item) ?: false
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }

    fun setOnClickListener(onClickListener: OnClickListener) {
        this.onClickListener = onClickListener
    }

    fun onLongClickListener(onLongClickListener: OnLongClickListener){
        this.onLongClickListener = onLongClickListener
    }

    interface OnClickListener {
        fun onClick(position: Int, track: Track)
    }

    interface OnLongClickListener {
        fun onLongClick(position: Int, track: Track): Boolean
    }

    private fun clickDebounce(): Boolean {
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
}

