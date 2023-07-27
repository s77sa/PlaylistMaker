package com.example.playlistmaker.ui.settings

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.Switch
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.playlistmaker.R
import org.koin.androidx.viewmodel.ext.android.viewModel

class SettingsActivity : AppCompatActivity() {

    private val viewModel by viewModel<SettingsViewModel>()

    @SuppressLint("UseSwitchCompatOrMaterialCode")
    private lateinit var themeSwitcher: Switch
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        themeSwitcher = findViewById<Switch>(R.id.switch_theme)
        initListenerThemeSwitch()
        initListeners()
        viewModel.getThemeFromSharedPrefs()
        initObservers()
    }

    private fun initObservers() {
        viewModel.darkTheme.observe(this) {
            themeSwitchCheck(it)
        }
    }

    private fun initListeners(){
        findViewById<ImageView>(R.id.iv_settings_back).setOnClickListener {
            this.finish()
        }
        findViewById<TextView>(R.id.tv_support).setOnClickListener {
            externalNavigatorEmail()
        }
        findViewById<TextView>(R.id.tv_share).setOnClickListener {
            externalNavigatorSend()
        }
        findViewById<TextView>(R.id.tv_terms).setOnClickListener {
            externalNavigatorOpenLink()
        }
    }

    private fun themeSwitchCheck(isDarkTheme: Boolean) {
        themeSwitcher.isChecked = isDarkTheme
    }

    private fun initListenerThemeSwitch() {
        themeSwitcher.setOnCheckedChangeListener { _, checked ->
            viewModel.saveAndApplyTheme(checked)
        }
    }

    private fun showToastErrorMessage(){
        Toast.makeText(this, R.string.external_navigator_error, Toast.LENGTH_SHORT).show()
    }

    private fun externalNavigatorEmail() {
        val result: String? = viewModel.callEmailIntent(
            getString(R.string.email_dest),
            getString(R.string.email_subject),
            getString(R.string.email_text)
        )
        if(!result.isNullOrEmpty()){
            showToastErrorMessage()
        }
    }
    private fun externalNavigatorSend(){
        val result: String? = viewModel.callSendIntent(getString(R.string.link_practicum_ad))
        if(!result.isNullOrEmpty()){
            showToastErrorMessage()
        }
    }

    private fun externalNavigatorOpenLink(){
        val result: String? = viewModel.callOpenLinkIntent(getString(R.string.link_terms))
        if(!result.isNullOrEmpty()){
            showToastErrorMessage()
        }
    }
}