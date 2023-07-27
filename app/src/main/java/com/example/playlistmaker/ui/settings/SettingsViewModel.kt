package com.example.playlistmaker.ui.settings

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.playlistmaker.domain.settings.sharedprefs.ThemeInteractor
import com.example.playlistmaker.domain.sharing.ExternalNavigatorInteractor
import com.example.playlistmaker.ui.ThemeSwitcher

class SettingsViewModel(
    private var themeInteractor: ThemeInteractor?,
    private val externalNavigator: ExternalNavigatorInteractor?
) : ViewModel() {
    init {
        Log.d("my_tag", "init - Settings ViewModel}")
    }
    private val darkThemeMutable = MutableLiveData<Boolean>()
    val darkTheme: LiveData<Boolean> = darkThemeMutable

    fun getThemeFromSharedPrefs(){
        darkThemeMutable.value = themeInteractor?.restoreTheme()
    }

    fun saveAndApplyTheme(value: Boolean){
        setThemeSharedPrefs(value)
        switchTheme()
    }

    private fun setThemeSharedPrefs(value: Boolean){
        if(themeInteractor != null) {
            themeInteractor!!.saveTheme(value)
            darkThemeMutable.value = value
        }
    }

    private fun switchTheme(){
        darkTheme.value?.let { ThemeSwitcher.switchTheme(it) }
    }

    fun callSendIntent(message: String): String?{
        return externalNavigator?.intentSend(message)
    }

    fun callEmailIntent(address: String, subject: String, text: String): String?{
        return externalNavigator?.intentEmail(
            address = address,
            subject = subject,
            text = text
        )
    }

    fun callOpenLinkIntent(link: String): String?{
        return externalNavigator?.intentOpenLink(link)
    }
}