package com.example.playlistmaker.domain.models.track

import java.io.Serializable

data class Tracks(
    val resultCount: Int,
    val results: MutableList<Track>
): Serializable