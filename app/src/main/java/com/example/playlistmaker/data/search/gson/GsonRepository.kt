package com.example.playlistmaker.data.search.gson

import com.google.gson.Gson

interface GsonRepository {
    fun getGson(): Gson
}
