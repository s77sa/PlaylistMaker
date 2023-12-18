package com.example.playlistmaker.data.models

import java.io.Serializable

data class Track(
    val trackName: String? = null,
    val artistName: String? = null,
    val trackTimeMillis: Int? = null,
    val artworkUrl100: String? = null,
    val trackId: Int? = null,
    val collectionName: String? = null,
    val releaseDate: String? = null,
    val primaryGenreName: String? = null,
    val country: String? = null,
    val previewUrl: String? = null
) : Serializable
