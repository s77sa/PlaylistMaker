package com.example.playlistmaker.ui.root

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.playlistmaker.domain.settings.sharedprefs.ThemeInteractor
import com.example.playlistmaker.ui.ThemeSwitcher

class RootViewModel(
    private var themeInteractor: ThemeInteractor
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
}
