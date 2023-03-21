package com.example.playlistmaker

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.example.playlistmaker.utils.Intents

class SettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        onClickListeners() // Запуск всех прослушивателей на активити
    }

    private fun onClickListeners(){
        onClickReturn()
        onClickShare()
        onClickSupport()
        onClickTerms()
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
        val intents = Intents()
        when (view.id) {
            R.id.iv_settings_back -> this.finish()
            R.id.tv_share -> intents.intentSend(this, getString(R.string.link_practicum_ad))
            R.id.tv_support -> intents.intentEmail(this, getString(R.string.email_dest), getString(R.string.email_text))
            R.id.tv_terms -> intents.intentOpenLink(this, getString(R.string.link_terms))
        }
    }
}