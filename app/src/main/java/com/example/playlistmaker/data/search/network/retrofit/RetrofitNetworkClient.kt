package com.example.playlistmaker.data.search.network.retrofit

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import com.example.playlistmaker.R
import com.example.playlistmaker.data.search.network.NetworkClient
import com.example.playlistmaker.data.search.network.Response
import com.example.playlistmaker.data.search.network.TracksSearchRequest
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor

class RetrofitNetworkClient(
    private val context: Context,
): NetworkClient {

    private val interceptorEnable: Boolean = true

    private fun getBaseUrl(): String{
        return context.getString(R.string.searchBaseUrl)
    }

    private val retrofit: Retrofit = Retrofit.Builder().apply {
        if(interceptorEnable) client(interceptorClient())
        baseUrl(getBaseUrl())
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
        if (!isConnected()) {
            return Response().apply { resultCode = -1 }
        }
        if (dto !is TracksSearchRequest) {
            return Response().apply { resultCode = 400 }
        }

        val response = itunesService.searchTracks(dto.expression).execute()
        val body = response.body()
        return if (body != null) {
            body.apply { resultCode = response.code() }
        } else {
            Response().apply { resultCode = response.code() }
        }
    }

    private fun isConnected(): Boolean {
        val connectivityManager = context.getSystemService(
            Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val capabilities = connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        if (capabilities != null) {
            when {
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> return true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> return true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> return true
            }
        }
        return false
    }
}