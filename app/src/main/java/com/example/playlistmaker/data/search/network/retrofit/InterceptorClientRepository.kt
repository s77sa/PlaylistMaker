package com.example.playlistmaker.data.search.network.retrofit

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor

interface InterceptorClientRepository {
    fun interceptorClient(): OkHttpClient
    fun interceptorInit(): HttpLoggingInterceptor
}