package com.example.playlistmaker.data.search.sharedprefs

import com.example.playlistmaker.data.search.models.Tracks

interface UserHistorySharedPrefsRepository {

    fun saveUserHistory(tracks: Tracks)

    fun restoreUserHistory(): Tracks?
}