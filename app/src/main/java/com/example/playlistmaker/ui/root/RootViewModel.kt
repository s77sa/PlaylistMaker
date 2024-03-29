package com.example.playlistmaker.ui.root

import androidx.lifecycle.ViewModel
import com.example.playlistmaker.domain.settings.sharedprefs.ThemeInteractor
import com.example.playlistmaker.ui.utils.ThemeSwitcher

class RootViewModel(
    private var themeInteractor: ThemeInteractor
) : ViewModel() {
    init {
        switchTheme()
    }

    private fun getThemeFromSharedPrefs(): Boolean {
        return themeInteractor.restoreTheme()
    }

    fun switchTheme() {
        ThemeSwitcher.switchTheme(getThemeFromSharedPrefs())
    }
}
