package com.example.playlistmaker.retrofit

data class Tracks(
    //val tracks: List<Track>
    val resultCount: Int,
    val results: MutableList<Track>
)