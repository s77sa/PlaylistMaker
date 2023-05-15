package com.example.playlistmaker.retrofit

data class Tracks(
    val resultCount: Int,
    val results: MutableList<Track>
)