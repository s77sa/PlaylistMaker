package com.example.playlistmaker.data.search.network.retrofit

sealed class Resource<T>(val data: T? = null, val message: ConnectionStatus = ConnectionStatus.SUCCESS) {
    class Success<T>(data: T): Resource<T>(data)
    class Error<T>(message: ConnectionStatus, data: T? = null): Resource<T>(data, message)
}