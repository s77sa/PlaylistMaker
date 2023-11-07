package com.example.playlistmaker.data.search.network.retrofit.models

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ItunesApiService {
    @GET("/search?entity=song")
    fun searchTracks(@Query("term") text: String): Call<TracksSearchResponse>

    @GET("/search?entity=song")
    suspend fun searchTracksSuspend(@Query("term") text: String): TracksSearchResponse
}