package com.example.playlistmaker.domain.settings.sharedprefs.impl

import com.example.playlistmaker.domain.settings.sharedprefs.ThemeRepository
import com.example.playlistmaker.domain.settings.sharedprefs.ThemeInteractor

class ThemeInteractorImpl(private val repository: ThemeRepository):
    ThemeInteractor
{
    override fun saveTheme(status: Boolean) {
        repository.saveTheme(status)
    }

    override fun restoreTheme(): Boolean {
        return repository.restoreTheme()
    }
}
