package com.example.playlistmaker.ui.search.recyclerview

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.R
import com.example.playlistmaker.data.search.models.Track
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SearchAdapter(private val data: MutableList<Track>) :
    RecyclerView.Adapter<SearchViewHolder>() {
    companion object {
        private const val CLICK_DEBOUNCE_DELAY = 1000L
    }

    private var isClickAllowed = true

    private var onClickJob: Job? = null
    private var onClickListener: OnClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.search_item, parent, false)
        return SearchViewHolder(view)
    }

    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
        val item: Track = data[position]
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
        fun onClick(position: Int, track: Track)
    }

    private fun clickDebounce(): Boolean {
        onClickJob?.cancel()
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            onClickJob = MainScope().launch {
                delay(CLICK_DEBOUNCE_DELAY)
                isClickAllowed = true
            }
        }
        return current
    }
}