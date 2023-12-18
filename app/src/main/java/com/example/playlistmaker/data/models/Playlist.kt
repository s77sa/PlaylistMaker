package com.example.playlistmaker.data.models

data class Playlist(
    val id: Int,
    val name: String,
    val description: String?,
    val imagePath: String?,
    val tracksCount: Int
)