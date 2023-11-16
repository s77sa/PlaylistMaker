package com.example.playlistmaker.data.search.network.retrofit.models

interface NetworkClient {
    suspend fun doRequest(dto: Any): Response
}