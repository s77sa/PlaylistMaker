package com.example.playlistmaker.ui.settings

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.playlistmaker.creator.Creator
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

    fun callSendIntent(message: String){
        externalNavigator?.intentSend(message)
    }

    fun callEmailIntent(address: String, subject: String, text: String){
        externalNavigator?.intentEmail(
            address = address,
            subject = subject,
            text = text
        )
    }

    fun callOpenLinkIntent(link: String){
        externalNavigator?.intentOpenLink(link)
    }

    companion object {
        fun getViewModelFactory(): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                SettingsViewModel(
                    themeInteractor = Creator.provideThemeInteractor(this[APPLICATION_KEY] as Application),
                    externalNavigator = Creator.provideExternalNavigator(this[APPLICATION_KEY] as Application)
                )
            }
        }
    }
}