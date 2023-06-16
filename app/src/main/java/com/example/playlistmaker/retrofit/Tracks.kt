package com.example.playlistmaker.retrofit

import java.io.Serializable

data class Tracks(
    val resultCount: Int,
    val results: MutableList<Track>
): Serializable