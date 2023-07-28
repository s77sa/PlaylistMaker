package com.example.playlistmaker.data.search.network.retrofit

import retrofit2.Retrofit

interface RetrofitRepository {
    fun getRetrofit(): Retrofit
    fun getBaseUrl(): String
}