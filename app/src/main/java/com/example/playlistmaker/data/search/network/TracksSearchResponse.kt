package com.example.playlistmaker.data.search.network

class TracksSearchResponse(
    val searchType: String,
    val expression: String,
    val results: MutableList<TrackDto>
): Response()