package com.example.playlistmaker.data.search.network

import com.example.playlistmaker.data.search.models.Tracks
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface TracksApi {
    @GET("/search?entity=song")
    fun searchTracks(@Query("term") text: String): Call<Tracks>
}