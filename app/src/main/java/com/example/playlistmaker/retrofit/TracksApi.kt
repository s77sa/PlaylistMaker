package com.example.playlistmaker.retrofit

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface TracksApi {
    @GET("/search?entity=song")
    fun searchTracks(@Query("term") text: String): Call<Tracks>
//    suspend fun searchTracks(@Query("term") text: String): Tracks
}