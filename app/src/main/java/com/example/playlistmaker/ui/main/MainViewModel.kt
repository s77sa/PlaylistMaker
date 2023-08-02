package com.example.playlistmaker.ui.main

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.playlistmaker.domain.settings.sharedprefs.ThemeInteractor
import com.example.playlistmaker.ui.ThemeSwitcher
import com.example.playlistmaker.ui.library.LibraryActivity
import com.example.playlistmaker.ui.search.SearchActivity
import com.example.playlistmaker.ui.settings.SettingsActivity
import com.example.playlistmaker.ui.sharing.ActivityNavigator

class MainViewModel(
    private var themeInteractor: ThemeInteractor,
    private val activityNavigator: ActivityNavigator,
) : ViewModel(){
    init {
        Log.d("my_tag", "init - Main ViewModel}")
        switchTheme()
    }

    private fun getThemeFromSharedPrefs(): Boolean{
        return themeInteractor.restoreTheme()
    }

    fun switchTheme(){
        ThemeSwitcher.switchTheme(getThemeFromSharedPrefs())
    }

    fun callSettingsActivity(){
        activityNavigator.intentCall(SettingsActivity::class.java)
    }

    fun callLibraryActivity(){
        activityNavigator.intentCall(LibraryActivity::class.java)
    }

    fun callSearchActivity(){
        activityNavigator.intentCall(SearchActivity::class.java)
    }
}
