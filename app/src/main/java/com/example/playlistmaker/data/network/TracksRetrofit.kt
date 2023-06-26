package com.example.playlistmaker.data.network
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor

class TracksRetrofit (
    private val baseUrl: String,
    private val interceptorEnable: Boolean = false
){

    fun retrofitInit(): TracksApi {
        val retrofit: Retrofit = Retrofit.Builder().apply {
            if(interceptorEnable) client(interceptorClient())
            baseUrl(baseUrl)
            addConverterFactory(GsonConverterFactory.create())
        }.build()
        return retrofit.create(TracksApi::class.java)
    }

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
}