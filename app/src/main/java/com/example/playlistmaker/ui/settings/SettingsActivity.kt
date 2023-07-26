package com.example.playlistmaker.ui.settings

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.Switch
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import com.example.playlistmaker.R

class SettingsActivity : AppCompatActivity() {

    private lateinit var viewModel: SettingsViewModel

    @SuppressLint("UseSwitchCompatOrMaterialCode")
    private lateinit var themeSwitcher: Switch
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        themeSwitcher = findViewById<Switch>(R.id.switch_theme)
        onClickListeners()
        viewModel = ViewModelProvider(
            this,
            SettingsViewModel.getViewModelFactory()
        )[SettingsViewModel::class.java]
        viewModel.getThemeFromSharedPrefs()
        initObservers()
    }

    private fun initObservers(){
        viewModel.darkTheme.observe(this){
            themeSwitchCheck(it)
        }
    }

    private fun onClickListeners(){
        onClickReturn()
        onClickShare()
        onClickSupport()
        onClickTerms()
        onClickThemeSwitch()
    }

    private fun themeSwitchCheck(isDarkTheme: Boolean)
    {
        themeSwitcher.isChecked = isDarkTheme
    }

    private fun onClickThemeSwitch() {
        themeSwitcher.setOnCheckedChangeListener { _, checked ->
            viewModel.saveAndApplyTheme(checked)
        }
    }

    private fun onClickTerms(){ // Соглашение
        val item = findViewById<TextView>(R.id.tv_terms)
    item.setOnClickListener(clickListener())
    }

    private fun onClickSupport(){ // Поддержка
        val item = findViewById<TextView>(R.id.tv_support)
        item.setOnClickListener(clickListener())
    }

    private fun onClickShare(){ // Поделиться
        val item = findViewById<TextView>(R.id.tv_share)
        item.setOnClickListener(clickListener())
    }

    private fun onClickReturn(){ // Возврат на предыдущий экран
        val item = findViewById<ImageView>(R.id.iv_settings_back)
        item.setOnClickListener(clickListener())
    }

    private fun clickListener() = View.OnClickListener { view ->
        when (view.id) {
            R.id.iv_settings_back -> this.finish()
            R.id.tv_share -> viewModel.callSendIntent(getString(R.string.link_practicum_ad))
            R.id.tv_support -> viewModel.callEmailIntent(getString(R.string.email_dest), getString(R.string.email_subject) ,getString(R.string.email_text))
            R.id.tv_terms -> viewModel.callOpenLinkIntent(getString(R.string.link_terms))
        }
    }
}