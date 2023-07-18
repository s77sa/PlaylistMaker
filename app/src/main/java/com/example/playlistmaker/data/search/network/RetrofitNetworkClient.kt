package com.example.playlistmaker.data.search.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor

class RetrofitNetworkClient(
    private val searchBaseUrl: String
): NetworkClient {

    private val interceptorEnable: Boolean = true

    private val retrofit: Retrofit = Retrofit.Builder().apply {
        if(interceptorEnable) client(interceptorClient())
        baseUrl(searchBaseUrl)
        addConverterFactory(GsonConverterFactory.create())
    }.build()

    private val itunesService = retrofit.create(ItunesApiService::class.java)

    private fun interceptorClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(interceptorInit())
            .build()
    }

    private fun interceptorInit(): HttpLoggingInterceptor{
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        return interceptor
    }

    override fun doRequest(dto: Any): Response {
        return if (dto is TracksSearchRequest) {
            val resp = itunesService.searchTracks(dto.expression).execute()
            val body = resp.body() ?: Response()
            body.apply { resultCode = resp.code() }
        } else {
            Response().apply { resultCode = 400 }
        }
    }
}