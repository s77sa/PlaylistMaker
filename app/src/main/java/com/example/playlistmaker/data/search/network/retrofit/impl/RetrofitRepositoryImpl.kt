package com.example.playlistmaker.data.search.network.retrofit.impl

import android.content.Context
import com.example.playlistmaker.R
import com.example.playlistmaker.data.search.network.retrofit.InterceptorClientRepository
import com.example.playlistmaker.data.search.network.retrofit.RetrofitRepository
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

private const val INTERCEPTOR_ENABLE: Boolean = true

class RetrofitRepositoryImpl(
    private val context: Context,
    private val interceptorClientRepository: InterceptorClientRepository,
) : RetrofitRepository {

    override fun getRetrofit(): Retrofit {
        interceptorClientRepository.interceptorClient()
        return Retrofit.Builder().apply {
            if (INTERCEPTOR_ENABLE) client(interceptorClientRepository.interceptorClient())
            baseUrl(getBaseUrl())
            addConverterFactory(GsonConverterFactory.create())
        }.build()
    }

    override fun getBaseUrl(): String {
        return context.getString(R.string.searchBaseUrl)
    }
}