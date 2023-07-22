package com.example.playlistmaker.data.search.models

import java.io.Serializable

data class Track(
    val trackName: String,
    val artistName: String,
    val trackTimeMillis: Int,
    val artworkUrl100: String,
    val trackId: Int,
    val collectionName: String,
    val releaseDate: String,
    val primaryGenreName: String?,
    val country: String,
    val previewUrl: String
) : Serializable
