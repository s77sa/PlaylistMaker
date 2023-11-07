package com.example.playlistmaker.data.search.network.retrofit.models

interface NetworkClient {
    fun doRequest(dto: Any): Response
    suspend fun doRequestSuspend(dto: Any): Response
}