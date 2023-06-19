package com.example.playlistmaker.data.dto

class TracksSearchResponse(
    val searchType: String,
    val expression: String,
    val results: MutableList<TrackDto>
): Response()