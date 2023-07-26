package com.example.playlistmaker.ui.main

import android.app.Application
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.playlistmaker.creator.Creator
import com.example.playlistmaker.domain.settings.sharedprefs.ThemeInteractor
import com.example.playlistmaker.ui.ThemeSwitcher
import com.example.playlistmaker.ui.library.LibraryActivity
import com.example.playlistmaker.ui.search.SearchActivity
import com.example.playlistmaker.ui.settings.SettingsActivity
import com.example.playlistmaker.ui.sharing.ActivityNavigator

class MainViewModel(
    private var themeInteractor: ThemeInteractor?,
    private val activityNavigator: ActivityNavigator,
) : ViewModel(){
    init {
        Log.d("my_tag", "init - Main ViewModel}")
        switchTheme()
    }

    private fun getThemeFromSharedPrefs(): Boolean{
        return themeInteractor?.restoreTheme() ?: false
    }

    private fun switchTheme(){
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

    companion object {
        fun getViewModelFactory(): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                MainViewModel(
                    themeInteractor = Creator.provideThemeInteractor(this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as Application),
                    activityNavigator = ActivityNavigator(this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as Application)
                )
            }
        }
    }
}
