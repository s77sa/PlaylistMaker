package com.example.playlistmaker.data.models

import com.example.playlistmaker.domain.model.Track
import java.io.Serializable

data class Tracks(
    val resultCount: Int,
    val results: MutableList<Track>
): Serializable