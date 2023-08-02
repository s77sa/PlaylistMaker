package com.example.playlistmaker.data.search.network.retrofit.models

import com.example.playlistmaker.data.search.models.Track

class TracksSearchResponse(
    val results: MutableList<Track>
): Response()