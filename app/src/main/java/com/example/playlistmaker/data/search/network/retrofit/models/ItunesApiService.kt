package com.example.playlistmaker.data.search.network.retrofit.models

import retrofit2.http.GET
import retrofit2.http.Query

interface ItunesApiService {

    @GET("/search?entity=song")
    suspend fun searchTracks(@Query("term") text: String): TracksSearchResponse
}