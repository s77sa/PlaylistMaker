package com.example.playlistmaker.data.search.network.retrofit

import com.example.playlistmaker.data.search.models.Track
import com.example.playlistmaker.data.search.network.Response

class TracksSearchResponse(
    val results: MutableList<Track>
): Response()