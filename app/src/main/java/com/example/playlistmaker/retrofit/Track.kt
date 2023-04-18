package com.example.playlistmaker.retrofit

data class Track(
    val trackName: String,
    val artistName: String,
    val trackTimeMillis: Int,
    val  artworkUrl100: String
)
