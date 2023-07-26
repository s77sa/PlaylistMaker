package com.example.playlistmaker.data.search.network

interface NetworkClient {
    fun doRequest(dto: Any): Response
}