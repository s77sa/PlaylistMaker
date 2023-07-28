package com.example.playlistmaker.data.sharing

import android.content.SharedPreferences

interface SharedPreferencesRepository {
    fun getSharedPreferences(): SharedPreferences
}