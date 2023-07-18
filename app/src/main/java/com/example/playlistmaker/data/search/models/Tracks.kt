package com.example.playlistmaker.data.search.models

import java.io.Serializable

data class Tracks(
    val resultCount: Int,
    val results: MutableList<Track>
): Serializable