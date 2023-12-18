package com.example.playlistmaker.data.models

import java.io.Serializable

data class Tracks(
    val resultCount: Int,
    val results: MutableList<Track>
): Serializable