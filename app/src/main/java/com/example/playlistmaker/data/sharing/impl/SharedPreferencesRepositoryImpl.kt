package com.example.playlistmaker.data.sharing.impl

import android.content.Context
import android.content.SharedPreferences
import com.example.playlistmaker.data.sharing.SharedPreferencesRepository

const val PLAY_LIST_PREFERENCES = "play_list_preferences"

class SharedPreferencesRepositoryImpl(private val context: Context): SharedPreferencesRepository {

    override fun getSharedPreferences(): SharedPreferences {
        return context.getSharedPreferences(
        PLAY_LIST_PREFERENCES,
        Context.MODE_PRIVATE)
    }
}