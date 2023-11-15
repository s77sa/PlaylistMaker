package com.example.playlistmaker.ui.settings

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.playlistmaker.BuildConfig
import com.example.playlistmaker.domain.settings.sharedprefs.ThemeInteractor
import com.example.playlistmaker.domain.sharing.ExternalNavigatorInteractor
import com.example.playlistmaker.ui.utils.ThemeSwitcher

class SettingsFragmentViewModel(
    private var themeInteractor: ThemeInteractor?,
    private val externalNavigator: ExternalNavigatorInteractor?
) : ViewModel() {
    init {
        Log.d(BuildConfig.LOG_TAG, "init - Settings ViewModel}")
    }

    private val darkThemeMutable = MutableLiveData<Boolean>()
    val darkTheme: LiveData<Boolean> = darkThemeMutable

    fun getThemeFromSharedPrefs() {
        darkThemeMutable.value = themeInteractor?.restoreTheme()
    }

    fun saveAndApplyTheme(value: Boolean) {
        setThemeSharedPrefs(value)
        switchTheme()
    }

    private fun setThemeSharedPrefs(value: Boolean) {
        if (themeInteractor != null) {
            themeInteractor!!.saveTheme(value)
            darkThemeMutable.value = value
        }
    }

    private fun switchTheme() {
        darkTheme.value?.let { ThemeSwitcher.switchTheme(it) }
    }

    fun callSendIntent(message: String): String? = externalNavigator?.intentSend(message)

    fun callEmailIntent(address: String, subject: String, text: String): String? {
        return externalNavigator?.intentEmail(
            address = address,
            subject = subject,
            text = text
        )
    }

    fun callOpenLinkIntent(link: String): String? {
        return externalNavigator?.intentOpenLink(link)
    }
}