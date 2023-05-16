package com.example.playlistmaker

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView

class PlayerActivity : AppCompatActivity() {
    private lateinit var buttonBack: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player)
        buttonsInit()
        clickListenersInit()
    }

    private fun buttonsInit(){
        buttonBack = findViewById<ImageView>(R.id.iv_player_back)
    }

    private fun clickListenersInit(){
        buttonBack.setOnClickListener(clickListener())
    }

    private fun clickListener() = View.OnClickListener { view ->
        when (view.id) {
            R.id.iv_player_back -> this.finish()
        }
    }
}