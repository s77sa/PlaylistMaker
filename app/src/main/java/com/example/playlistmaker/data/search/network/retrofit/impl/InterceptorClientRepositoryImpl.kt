package com.example.playlistmaker.data.search.network.retrofit.impl

import com.example.playlistmaker.data.search.network.retrofit.InterceptorClientRepository
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor


class InterceptorClientRepositoryImpl : InterceptorClientRepository {

    override fun interceptorClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(interceptorInit())
            .build()
    }

    override fun interceptorInit(): HttpLoggingInterceptor {
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        return interceptor
    }
}