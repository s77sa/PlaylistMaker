package com.example.playlistmaker.data.search.gson.impl
import com.example.playlistmaker.data.search.gson.GsonRepository
import com.google.gson.Gson

class GsonRepositoryImpl : GsonRepository{
    override fun getGson(): Gson {
        return Gson()
    }
}